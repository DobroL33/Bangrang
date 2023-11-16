package com.ssafy.bangrang.global.batch;

import com.ssafy.bangrang.domain.map.entity.KoreaBorderArea;
import com.ssafy.bangrang.domain.map.entity.MemberMapArea;
import com.ssafy.bangrang.domain.map.model.vo.RegionType;
import com.ssafy.bangrang.domain.map.repository.KoreaBorderAreaRepository;
import com.ssafy.bangrang.domain.map.repository.MemberMapAreaRepository;
import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.entity.Friendship;
import com.ssafy.bangrang.domain.member.repository.AppMemberRepository;
import com.ssafy.bangrang.domain.member.repository.FriendshipRepository;
import com.ssafy.bangrang.domain.rank.entity.Ranking;
import com.ssafy.bangrang.domain.rank.repository.RankingRepository;
import com.ssafy.bangrang.global.fcm.api.request.SendAlarmRequestDto;
import com.ssafy.bangrang.global.fcm.entity.Alarm;
import com.ssafy.bangrang.global.fcm.model.vo.AlarmType;
import com.ssafy.bangrang.global.fcm.service.AlarmService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Geometry;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableBatchProcessing
public class BatchConfig {
    private final JobLauncher jobLauncher;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager batchTransactionManager;
    private final EntityManagerFactory entityManagerFactory; // EntityManagerFactory 주입
    private final AppMemberRepository appmemberRepository;
    private final KoreaBorderAreaRepository koreaBorderAreaRepository;
    private final MemberMapAreaRepository memberMapAreaRepository;
    private final RankingRepository rankingRepository;
    private final FriendshipRepository friendshipRepository;
    private final AlarmService alarmService;

    private static final int BATCH_SIZE = 5;

    private static Map<RegionType, Long> curRank;
    private static List<KoreaBorderArea> koreaBorderAreas;
    // 각 지역의 너비 정보를 가지고 있는 Map
    private static Map<RegionType, Double> koreaBorderAreaMap;

    private static Long totalMemberCnt;


    @PostConstruct
    public void init() {
        initTotalMemberCnt();
        initKoreaBorderAreas();
        initCurRank();
    }

    private void initTotalMemberCnt() {
        totalMemberCnt = appmemberRepository.count();
    }

    private void initKoreaBorderAreas() {
        koreaBorderAreas = koreaBorderAreaRepository.findAll();
        System.out.println("koreaBorderAreas.size() = " + koreaBorderAreas.size());
        koreaBorderAreaMap = new HashMap<>();

        double koreaArea = 0.0;

        for (KoreaBorderArea koreaBorderArea : koreaBorderAreas) {
            double area = koreaBorderArea.getShape().getArea();
            RegionType regionType = convertRegionType(koreaBorderArea.getIdx());
            koreaBorderAreaMap.put(regionType, area);
            koreaArea += area;
        }

        koreaBorderAreaMap.put(RegionType.KOREA, koreaArea);

    }

    private void initCurRank() {
        curRank = new HashMap<RegionType, Long>();

        for (RegionType region : RegionType.values()) {
            curRank.put(region, (long) 1);
        }
    }

    @Bean
    public Job myRegionRankingJob() {

        log.info("[ JOB ] myRegionRankingJob 수행중 ");
        log.info("1. 지역별 사용자 면적 계산 -> ");
        log.info("2. 랭킹 계산 -> ");
        log.info("3. 랭킹 관련 알림 보내기 ");
        return new JobBuilder("myRegionRankingJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(taskletStep())
                .next(getRegionMapAreaStep())
                .next(getMemberRankingStep())
                .next(postFCMStep())
                .build();
    }

    @Bean
    public Step taskletStep() {
        log.info("*****************************************************************************");
        log.info("HelloWorld!");
        log.info("In Batch!!!");
        log.info("*****************************************************************************");

        log.info("totalMemberCnt = " + totalMemberCnt);
        log.info("curRank = " + curRank);
        log.info("curRank.get(RegionType.KOREA) = " + curRank.get(RegionType.KOREA));
        log.info("curRank.size = " + curRank.values().size());
        log.info("koreaBorderAreas.size() = " + koreaBorderAreas.size());
        log.info("koreaBorderAreaMap = " + koreaBorderAreaMap);

        return new StepBuilder("first step", jobRepository)
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("Hello World");
                    log.info("*****************************************************************************");
                    log.info("HelloWorld!");
                    log.info("In Batch!!!");
                    log.info("In Progress!!!");
                    log.info("*****************************************************************************");

                    log.info("totalMemberCnt = " + totalMemberCnt);
                    log.info("curRank = " + curRank);
                    log.info("curRank.get(RegionType.KOREA) = " + curRank.get(RegionType.KOREA));
                    log.info("curRank.size = " + curRank.values().size());
                    log.info("koreaBorderAreas.size() = " + koreaBorderAreas.size());
                    log.info("koreaBorderAreaMap = " + koreaBorderAreaMap);
                    return RepeatStatus.FINISHED;
                }, batchTransactionManager)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step getRegionMapAreaStep() {
        log.info("[ STEP 01 ] 지역별 사용자 면적 계산");

        return new StepBuilder("region_map_area_step", jobRepository)
                .allowStartIfComplete(true)
                .<String, String>chunk(BATCH_SIZE, batchTransactionManager)
                .reader(regionMapAreaReader())
                .processor(memberMapAreaProcessor())
                .writer(memberMapAreaWriter())
                .build();
    }


    @Bean
    public Step getMemberRankingStep() {
        log.info("[ STEP 02 ] 랭킹 계산");

        return new StepBuilder("member_ranking_step", jobRepository)
                .allowStartIfComplete(true)
                .<String, String>chunk(BATCH_SIZE, batchTransactionManager)
                .reader(regionMapAreaReaderForRanking())
                .processor(rankMapAreaProcessor())
                .writer(rankingWriter())
                .build();
    }


    @Bean
    public Step postFCMStep() {
        log.info("[ STEP 03 ] FCM 메시지 발송");
        return new StepBuilder("post_fcm_step", jobRepository)
                .allowStartIfComplete(true)
                .<String, String>chunk(BATCH_SIZE, batchTransactionManager)
                .reader(memberReader())
                .processor(memberAlarmProcessor())
                .writer(alarmWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader regionMapAreaReader() {
        log.info("[ STPE 01 - READER ] mapArea 데이터 읽는 중... ");
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1); // 어제 날짜 계산

        // 12시 전에는 하루에 멤버당 RegionType.KOREA 하나씩만 생김
        return new JpaPagingItemReaderBuilder<MemberMapArea>()
                .name("memberMapAreaReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select m from MemberMapArea m where FUNCTION('DATE',m.createdAt) >= :yesterday and FUNCTION('DATE',m.createdAt) < :today and m.regionType = :regionType")
                .parameterValues(Map.of("yesterday", yesterday, "today", today, "regionType", RegionType.KOREA))
                .pageSize(1000)
                .build();
    }

    @Bean
    public ItemProcessor<MemberMapArea, List<MemberMapArea>> memberMapAreaProcessor() {
        log.info("[ STPE 01 - PROCESSOR ] mapArea 데이터 가공 중... ");
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1); // 어제 날짜를 구함

        return memberMapArea ->
                                koreaBorderAreas.stream().map(koreaBorderArea -> {
                                    // 교집합이 없는 경우 getArea -> 0.0을 반환
                                    Geometry shape = koreaBorderArea.getShape().intersection(memberMapArea.getShape());
                                    return MemberMapArea.builder()
                                            .shape(shape)
                                            .dimension(shape.getArea())
                                            .appMember(memberMapArea.getAppMember())
                                            .regionType(convertRegionType(koreaBorderArea.getIdx()))
                                            .customDate(yesterday)
                                            .build();

                                }).collect(Collectors.toList());

    }


    @Bean
    @Transactional
    public ItemWriter<List<MemberMapArea>> memberMapAreaWriter() {
        log.info("[ STPE 01 - WRITER ] mapArea 데이터 저장 중... ");
        return itemLists -> {
            for (List<MemberMapArea> itemList : itemLists) {
                itemList.forEach(item -> {
                    log.info("[ STEP 01 - WRITER ]: "+item);
                    memberMapAreaRepository.save(item);
                });
            }
        };
    }

    private RegionType convertRegionType(Long idx) {
        switch (idx.intValue()) {
            case 1:
                return RegionType.GANGWON;
            case 2:
                return RegionType.GYEONGGI;
            case 3:
                return RegionType.GYEONGNAM;
            case 4:
                return RegionType.GYEONGBUK;
            case 5:
                return RegionType.GWANGJU;
            case 6:
                return RegionType.DAEGU;
            case 7:
                return RegionType.DAEJEON;
            case 8:
                return RegionType.BUSAN;
            case 9:
                return RegionType.SEOUL;
            case 10:
                return RegionType.SEJONG;
            case 11:
                return RegionType.ULSAN;
            case 12:
                return RegionType.INCHEON;
            case 13:
                return RegionType.JEOLLANAM;
            case 14:
                return RegionType.JEOLLABUK;
            case 15:
                return RegionType.JEJU;
            case 16:
                return RegionType.CHUNGNAM;
            case 17:
                return RegionType.CHUNGBUK;
            default:
                return RegionType.KOREA;
        }
    }

    @Bean
    public JpaPagingItemReader regionMapAreaReaderForRanking() {
        log.info("[ STPE 02 - READER ] mapArea 데이터 읽는 중... ");
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1); // 어제 날짜 계산

        return new JpaPagingItemReaderBuilder<MemberMapArea>()
                .name("memberMapAreaReaderForRanking")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select m from MemberMapArea m where FUNCTION('DATE',m.createdAt) >= :yesterday and FUNCTION('DATE',m.createdAt) < :today order by m.regionType, m.dimension desc")
                .parameterValues(Map.of("yesterday", yesterday, "today", today))
                .pageSize(1000)
                .build();
    }

    @Bean
    public ItemProcessor<MemberMapArea, Ranking> rankMapAreaProcessor() {
        log.info("[ STPE 02 - PROCESSOR ] mapArea 데이터를 등수 매기는 중... ");

        return memberMapArea -> {
                Long rank = curRank.getOrDefault(memberMapArea.getRegionType(), (long) 1);
                curRank.put(memberMapArea.getRegionType(), rank + 1);

                Double rating = (double) rank / totalMemberCnt;
                Double percent = (memberMapArea.getShape().getArea() / koreaBorderAreaMap.get(memberMapArea.getRegionType())) * 100.0;

                return Ranking.builder()
                        .regionType(memberMapArea.getRegionType())
                        .rank(rank)
                        .rating(rating)
                        .percent(percent)
                        .appMember(memberMapArea.getAppMember())
                        .build();
            };
    }

    @Bean
    @Transactional
    public ItemWriter<Ranking> rankingWriter() {
        log.info("[ STPE 02 - WRITER ] 등수 데이터 저장 중... ");
//        return itemLists -> {
//            for (List<Ranking> itemList : itemLists) {
//                itemList.forEach(ranking -> {
//                    log.info("[ STEP 02 - WRITER ] 등수 데이터 "+ranking);
//                    rankingRepository.save(ranking);
//                });
//            }
//        };
        return chunk -> {
            List<? extends Ranking> items = chunk.getItems();

            for(Ranking ranking : items){
                log.info("[ STEP 02 - WRITER ] 등수 데이터 "+ranking);
                rankingRepository.save(ranking);
            }
        };
    }

//    @Bean
//    public JpaPagingItemReader memberReader() {
//        log.info("[ STPE 03 - READER ] member 데이터 읽는 중... ");
//
//        return new JpaPagingItemReaderBuilder<AppMember>()
//                .name("memberReader")
//                .entityManagerFactory(entityManagerFactory)
//                .queryString("select m from AppMember m where m.alarms = :alarms")
//                .parameterValues(Map.of("alarms", true))
//                .pageSize(1000)
//                .build();
//    }

    @Bean
    public JpaPagingItemReader memberReader() {
        log.info("[ STPE 03 - READER ] member 데이터 읽는 중... ");

        return new JpaPagingItemReaderBuilder<AppMember>()
                .name("memberReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select am from AppMember am where am.alarms = :alarms")
                .parameterValues(Map.of("alarms", true))
                .pageSize(1000)
                .build();
    }

    @Bean
    public ItemProcessor<AppMember, List<Alarm>> memberAlarmProcessor() {
        log.info("[ STPE 03 - PROCESSOR ] alarm 발송 중... ");
        return appMember -> {
            LocalDate today = LocalDate.now();
            LocalDate yesterday = today.minusDays(1);

            List<Alarm> result = new ArrayList<>();

            List<Ranking> memberTodayRankingList = rankingRepository.findRankingByAppMemberAndCreatedAt(appMember.getIdx(), today);
            List<Ranking> memberYesterdayRankingList = rankingRepository.findRankingByAppMemberAndCreatedAt(appMember.getIdx(), yesterday);

            Map<RegionType, Ranking> memberTodayRankingMap = new HashMap();
            Map<RegionType, Ranking> memberYesterdayRankingMap = new HashMap<>();

            memberTodayRankingList.stream().forEach(ranking -> {
                if(!memberTodayRankingMap.containsKey(ranking.getRegionType())){
                    memberTodayRankingMap.put(ranking.getRegionType(), ranking);
                }
            });

            memberYesterdayRankingList.stream().forEach(ranking -> {
                if(!memberYesterdayRankingMap.containsKey(ranking.getRegionType())){
                    memberYesterdayRankingMap.put(ranking.getRegionType(), ranking);
                }
            });


//                        Map<RegionType, Ranking> appMemberRankingYesterday =

            // 1. 경쟁 알림 전송
            List <Alarm> competitorAlarm = getCompetitorAlarm(appMember, memberTodayRankingMap, memberYesterdayRankingMap, today, yesterday);

            // 2. 정복율 알림 전송
            List<Alarm> percentAlarm = getPercentAlarm(appMember, memberTodayRankingMap, memberYesterdayRankingMap);

            return Stream.concat(competitorAlarm.stream(), percentAlarm.stream()).collect(Collectors.toList());

        };

//        return items -> {
//            return items.stream().map(appMember -> {
//                        LocalDate today = LocalDate.now();
//                        LocalDate yesterday = today.minusDays(1);
//
//                        List<Ranking> memberTodayRankingList = rankingRepository.findRankingByAppMemberAndCreatedAt(appMember.getIdx(), today);
//                        List<Ranking> memberYesterdayRankingList = rankingRepository.findRankingByAppMemberAndCreatedAt(appMember.getIdx(), yesterday);
//
//                        Map<RegionType, Ranking> memberTodayRankingMap = new HashMap();
//                        Map<RegionType, Ranking> memberYesterdayRankingMap = new HashMap<>();
//
//                        memberTodayRankingList.stream().forEach(ranking -> {
//                            if(!memberTodayRankingMap.containsKey(ranking.getRegionType())){
//                                memberTodayRankingMap.put(ranking.getRegionType(), ranking);
//                            }
//                        });
//
//                        memberYesterdayRankingList.stream().forEach(ranking -> {
//                            if(!memberYesterdayRankingMap.containsKey(ranking.getRegionType())){
//                                memberYesterdayRankingMap.put(ranking.getRegionType(), ranking);
//                            }
//                        });
//
//
////                        Map<RegionType, Ranking> appMemberRankingYesterday =
//
//                        // 1. 경쟁 알림 전송
//                        List <Alarm> competitorAlarm = getCompetitorAlarm(appMember, memberTodayRankingMap, memberYesterdayRankingMap, today, yesterday);
//
//                        // 2. 정복율 알림 전송
//                        List<Alarm> percentAlarm = getPercentAlarm(appMember, memberTodayRankingMap, memberYesterdayRankingMap);
//
//                        return Stream.concat(competitorAlarm.stream(), percentAlarm.stream()).collect(Collectors.toList());
//                    })
//                    .flatMap(List::stream)
//                    .collect(Collectors.toList());
//        };
    }

    /**
     * 10% 단위로 정복도가 달성되었을 때 Member에게 알람 보내기 위한 함수
     * @param appMember
     * @param memberTodayRankingMap
     * @param memberYesterdayRankingMap
     * @return
     */
    private List<Alarm> getPercentAlarm(AppMember appMember, Map<RegionType, Ranking> memberTodayRankingMap, Map<RegionType, Ranking> memberYesterdayRankingMap) {
        List<Alarm> result = new ArrayList<>();
        for(RegionType regionType : memberTodayRankingMap.keySet()){
            Ranking todayRanking = memberTodayRankingMap.get(regionType);
            String region = todayRanking.getRegionType().getKoreanName();
            int todayPercent = todayRanking.getPercent().intValue()*100;
            String content = region + " 정복도 "+todayPercent+"%를 달성했습니다!";
            if(!memberYesterdayRankingMap.containsKey(regionType)){
                result.add(Alarm
                        .builder()
                                .type(AlarmType.NOTIFICATION)
                                .content(content)
                                .appMember(appMember)
                        .build());
            }else{
                Ranking yesterdayRanking = memberYesterdayRankingMap.get(regionType);
                int yesterdayPercent = yesterdayRanking.getPercent().intValue()*100;

                if(!isSamePercent(todayPercent, yesterdayPercent)){
                    result.add(Alarm
                            .builder()
                            .type(AlarmType.NOTIFICATION)
                            .content(content)
                            .appMember(appMember)
                            .build());
                }
            }
        }
        return result;
    }

    private boolean isSamePercent(int todayPercent, int yesterdayPercent) {
        if(todayPercent / 10 == yesterdayPercent / 10) return true;
        else return false;
    }

    /**
     * 전국 랭킹에서 친구가 나보다 앞선 경우 Member에게 알람 보내기 위한 함수
     * @param appMember
     * @param memberTodayRankingMap
     * @param memberYesterdayRankingMap
     * @return
     */
    private List<Alarm> getCompetitorAlarm(AppMember appMember, Map<RegionType, Ranking> memberTodayRankingMap, Map<RegionType, Ranking> memberYesterdayRankingMap, LocalDate today, LocalDate yesterday) {
        List<Alarm> result = new ArrayList<>();

        // 친구 리스트를 구한다.
        List<Long> friends = friendshipRepository.findAllByAppMemberJpql(appMember.getIdx()).stream().map(Friendship::getIdx).collect(Collectors.toList());

        // 친구들의 어제 오늘 Ranking을 구한다.
        List<Ranking> friendsRanking = rankingRepository.findFriendRankingBetweenDateByRegionType(yesterday, today, friends, RegionType.KOREA);
        Map<Long, List<Ranking>> friendsRankingMap = new HashMap<>();

        friendsRanking.stream().forEach(ranking -> {
            Long key = ranking.getIdx();
            if(friendsRankingMap.containsKey(key)){
                friendsRankingMap.get(key).add(ranking);
            }else{
                List<Ranking> value = new ArrayList<>();
                value.add(ranking);
                friendsRankingMap.put(key, value);
            }
        });

        // 어제 사용자보다 랭킹이 낮고
        // 오늘 사용자보다 랭킹이 높은
        // 친구를 구한다.

        // 정렬
        // 친구가 신규 회원인 경우 -> 오늘만 비교
        // 친구가 없어진 회원인 경우 -> 1. deletedAt이 지난 경우 아무것도 하지 않음 2. 지나지 않고 어제만 있는 경우 아무것도 하지 않음 3. 둘 다 있는 경우 비교
        // 결론: size가 1이고 오늘 데이터인 경우에는 alarm을 create
        // size가 2이상일 때
        // 일단 오늘 데이터 있는지 확인
        // 없으면 아무것도 하지 않음
        // 있으면 어제 데이터 있는지 확인
        // 어제 데이터 없으면 alarm create
        // 있으면 두 개만 남기기
        for(Long friendIdx : friendsRankingMap.keySet()){
            List<Ranking> friendLankings = friendsRankingMap.get(friendIdx);
            Collections.sort(friendLankings, (o1, o2) -> o1.getCreatedAt().compareTo(o2.getCreatedAt()));

            if(friendLankings.size() < 1) continue;
            else if(friendLankings.size() == 1){
                if(friendLankings.get(0).getCreatedAt().toLocalDate().isEqual(today)){
                    Optional<AppMember> friendAppMember = appmemberRepository.findByIdx(friendIdx);
                    if(friendAppMember.isEmpty()) continue;
                    String content = "이런 "+friendAppMember.get().getNickname()+"님이 당신을 이겼습니다!" ;

                    result.add(Alarm
                            .builder()
                                    .type(AlarmType.RANKING)
                                    .content(content)
                                    .appMember(appMember)
                            .build());

                }
            }else{
                Ranking yesterdayFriendRanking = null;
                Ranking todayFriendRanking = null;

                for(Ranking friendRanking : friendLankings){
                    if(yesterdayFriendRanking == null && friendRanking.getCreatedAt().toLocalDate().isEqual(yesterday)) yesterdayFriendRanking = friendRanking;
                    if(todayFriendRanking == null && friendRanking.getCreatedAt().toLocalDate().isEqual(today)) todayFriendRanking = friendRanking;
                }

                if(todayFriendRanking == null) continue;

                if(yesterdayFriendRanking == null){
                    Optional<AppMember> friendAppMember = appmemberRepository.findByIdx(friendIdx);
                    if(friendAppMember.isEmpty()) continue;
                    String content = "이런 "+friendAppMember.get().getNickname()+"님이 당신을 이겼습니다!" ;
                    result.add(Alarm
                            .builder()
                            .type(AlarmType.RANKING)
                            .content(content)
                            .appMember(appMember)
                            .build());
                }else{
                    if(!memberYesterdayRankingMap.containsKey(RegionType.KOREA) || !memberTodayRankingMap.containsKey(RegionType.KOREA)) continue;

                    if(yesterdayFriendRanking.getRank() < memberYesterdayRankingMap.get(RegionType.KOREA).getRank() && todayFriendRanking.getRank() > memberTodayRankingMap.get(RegionType.KOREA).getRank()){
                        Optional<AppMember> friendAppMember = appmemberRepository.findByIdx(friendIdx);
                        if(friendAppMember.isEmpty()) continue;
                        String content = "이런 "+friendAppMember.get().getNickname()+"님이 당신을 이겼습니다!" ;
                        result.add(Alarm
                                .builder()
                                .type(AlarmType.RANKING)
                                .content(content)
                                .appMember(appMember)
                                .build());
                    }
                }


            }
        }
        return result;
    }

    @Bean
    public ItemWriter<List<Alarm>> alarmWriter() {
        log.info("[ STPE 03 - WRITER ] 알람 데이터 저장 중... ");

        return itemLists -> {
            for (List<Alarm> itemList : itemLists) {
                itemList.forEach(alarm -> {
                    log.info(alarm.getContent());
                    try {
                        alarmService.sendAlarm(alarm.getAppMember().getIdx(), SendAlarmRequestDto
                                .builder()
                                .type(alarm.getType())
                                .content(alarm.getContent())
                                .eventIdx(alarm.getEventIdx())
                                .build());
                    } catch (Exception e) {
                        log.error("Error sending alarm for " + alarm.getAppMember().getIdx(), e);
                    }
                });
            }
        };
    }

}


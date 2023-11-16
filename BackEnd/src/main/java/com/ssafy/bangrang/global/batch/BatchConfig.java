package com.ssafy.bangrang.global.batch;

import com.ssafy.bangrang.domain.map.entity.KoreaBorderArea;
import com.ssafy.bangrang.domain.map.entity.MemberMapArea;
import com.ssafy.bangrang.domain.map.model.vo.RegionType;
import com.ssafy.bangrang.domain.map.repository.KoreaBorderAreaRepository;
import com.ssafy.bangrang.domain.map.repository.MemberMapAreaRepository;
import com.ssafy.bangrang.domain.member.repository.MemberRepository;
import com.ssafy.bangrang.domain.rank.entity.Ranking;
import com.ssafy.bangrang.domain.rank.repository.RankingRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableBatchProcessing
public class BatchConfig {
    private final JobLauncher jobLauncher;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager batchTransactionManager;
    private final EntityManagerFactory entityManagerFactory; // EntityManagerFactory 주입
    private final MemberRepository memberRepository;
    private final KoreaBorderAreaRepository koreaBorderAreaRepository;
    private final MemberMapAreaRepository memberMapAreaRepository;
    private final RankingRepository rankingRepository;
    private static final int BATCH_SIZE = 5;

    private static Map<RegionType, Long> curRank;
    private static List<KoreaBorderArea> koreaBorderAreas;
    // 각 지역의 너비 정보를 가지고 있는 Map
    private static Map<RegionType, Double> koreaBorderAreaMap;

    private static Long totalMemberCnt;



    @PostConstruct
    public void init(){
        initTotalMemberCnt();
        initKoreaBorderAreas();
        initCurRank();
    }

    private void initTotalMemberCnt(){
        totalMemberCnt = memberRepository.count();
    }

    private void initKoreaBorderAreas() {
        koreaBorderAreas = koreaBorderAreaRepository.findAll();
        System.out.println("koreaBorderAreas.size() = " + koreaBorderAreas.size());
        koreaBorderAreaMap = new HashMap<>();

        double koreaArea = 0.0;

        for(KoreaBorderArea koreaBorderArea : koreaBorderAreas){
            double area = koreaBorderArea.getShape().getArea();
            RegionType regionType = convertRegionType(koreaBorderArea.getIdx());
            koreaBorderAreaMap.put(regionType, area);
            koreaArea += area;
        }

        koreaBorderAreaMap.put(RegionType.KOREA, koreaArea);

    }

    private void initCurRank() {
        curRank = new HashMap<RegionType, Long>();

        for(RegionType region : RegionType.values()){
            curRank.put(region, (long)1);
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
//                .next(postFCMStep())
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
    public Step getMemberRankingStep(){
        log.info("[ STEP 02 ] 랭킹 계산");

        return new StepBuilder("member_ranking_step", jobRepository)
                .allowStartIfComplete(true)
                .<String, String>chunk(BATCH_SIZE, batchTransactionManager)
                .reader(regionMapAreaReaderForRanking())
                .processor(rankMapAreaProcessor())
                .writer(rankingWriter())
                .build();
    }


//    @Bean
//    public Step postFCMStep(){
//        log.info("[ STEP 03 ] FCM 메시지 발송");
//        return new StepBuilder("post_fcm_step", jobRepository)
//                .allowStartIfComplete(true)
//                .<String, String>chunk(BATCH_SIZE, batchTransactionManager)
//                .reader(reader())
//                .writer(writer())
//                .build();
//    }

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
    public ItemProcessor<List<MemberMapArea>, List<MemberMapArea>> memberMapAreaProcessor() {
        log.info("[ STPE 01 - PROCESSOR ] mapArea 데이터 가공 중... ");
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1); // 어제 날짜를 구함

        return items ->
                items
                        .stream()
                        .map(memberMapArea ->
                            koreaBorderAreas.stream().map(koreaBorderArea -> {
                                if(koreaBorderArea.getShape().intersects(memberMapArea.getShape())){
                                    Geometry shape = koreaBorderArea.getShape().intersection(memberMapArea.getShape());
                                    return MemberMapArea.builder()
                                            .shape(shape)
                                            .dimension(shape.getArea())
                                            .appMember(memberMapArea.getAppMember())
                                            .regionType(convertRegionType(koreaBorderArea.getIdx()))
                                            .customDate(yesterday)
                                            .build();
                                }else{
                                    return null;
                                }
                            }).filter(Objects::nonNull).collect(Collectors.toList())
                        )
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
    }



    @Bean
    public ItemWriter<List<MemberMapArea>> memberMapAreaWriter() {
        log.info("[ STPE 01 - WRITER ] mapArea 데이터 저장 중... ");
        return itemLists -> {
            for (List<MemberMapArea> itemList : itemLists) {
                itemList.forEach(item -> {
                    memberMapAreaRepository.save(item);
                });
            }
        };
    }

    private RegionType convertRegionType(Long idx) {
        switch (idx.intValue()){
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
    public ItemProcessor<List<MemberMapArea>, List<Ranking>> rankMapAreaProcessor(){
        log.info("[ STPE 02 - PROCESSOR ] mapArea 데이터를 등수 매기는 중... ");

        return items -> {
            return items.stream().map(memberMapArea -> {
                Long rank = curRank.getOrDefault(memberMapArea.getRegionType(), (long) 1);
                curRank.put(memberMapArea.getRegionType(), rank+1);

                Double rating = (double) rank / totalMemberCnt;
                Double percent = (memberMapArea.getShape().getArea()/koreaBorderAreaMap.get(memberMapArea.getRegionType())) * 100.0;

                return Ranking.builder()
                        .regionType(memberMapArea.getRegionType())
                        .rank(rank)
                        .rating(rating)
                        .percent(percent)
                        .appMember(memberMapArea.getAppMember())
                        .build();
            }).collect(Collectors.toList());
        };
    }

    @Bean
    public ItemWriter<List<Ranking>> rankingWriter(){
        log.info("[ STPE 02 - WRITER ] 등수 데이터 저장 중... ");
        return itemLists  -> {
            for(List<Ranking> itemList : itemLists){
                itemList.forEach(ranking -> {
                    rankingRepository.save(ranking);
                });
            }
        };
    }


}


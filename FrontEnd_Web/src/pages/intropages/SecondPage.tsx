import React, { useState } from "react";
import { useSprings, animated, to as interpolate } from "@react-spring/web";
import { useDrag } from "react-use-gesture";

import styles from "./SecondPage.module.css";

const cards = [
  "assets/images/제주.png",
  "assets/images/대전.png",
  "assets/images/서울.png",
  "assets/images/경주.png",
  "assets/images/부산.png",
  "assets/images/야경.png",
];

const moving = {};

const to = (i: number) => ({
  x: 0,
  y: i * -4,
  scale: 1,
  rot: -10 + Math.random() * 20,
  delay: i * 100,
});
const from = (_i: number) => ({ x: 0, rot: 0, scale: 1.5, y: -1000 });
const trans = (r: number, s: number) =>
  `perspective(1500px) rotateX(30deg) rotateY(${
    r / 10
  }deg) rotateZ(${r}deg) scale(${s})`;

const flying = window.innerWidth * 0.2;

function Deck({ setStack }: { setStack: (i: number) => void }) {
  const [gone] = useState(() => new Set());
  const [props, api] = useSprings(cards.length, (i) => ({
    ...to(i),
    from: from(i),
  }));

  const bind = useDrag(
    ({ args: [index], down, movement: [mx], direction: [xDir], velocity }) => {
      const trigger = velocity > 0.2;
      const dir = xDir < 0 ? -1 : 0;
      if (!down && trigger) gone.add(index);
      api.start((i) => {
        if (index !== i) return;
        const isGone = gone.has(index);
        const x = isGone
          ? (window.innerWidth - flying * 2) * dir
          : down
          ? mx
          : 1;
        const rot = mx / 100 + (isGone ? dir * 10 * velocity : 0);
        const scale = down ? 1.1 : 1;
        isGone ? setStack(--i) : null;
        return {
          x,
          rot,
          scale: 0.3,
          delay: undefined,
          config: { friction: 50, tension: down ? 300 : isGone ? 200 : 500 },
          opacity: 1,
        };
      });
      if (!down && gone.size === cards.length)
        setTimeout(() => {
          gone.clear();
          api((i: number) => to(i));
        }, 600);
    }
  );

  return (
    <>
      {props.map(({ x, y, rot, scale }, i) => (
        <animated.div className={styles.deck} key={i} style={{ x, y }}>
          <animated.div
            {...bind(i)}
            style={{
              transform: interpolate([rot, scale], trans),
              backgroundImage: `url(${cards[i]})`,
            }}
          />
        </animated.div>
      ))}
    </>
  );
}

export default function SecondPage() {
  const [stack, setStack] = useState<number>(-1);
  console.log({ stack });

  return (
    <div className={styles.Page}>
      <div className={styles.InfoContainer}>
        <img
          src="assets/images/Phone.png"
          style={{ position: "absolute", width: "350px", left: "10%" }}
          alt=""
        />
        <div className={styles.infos}></div>
      </div>
      <div className={styles.CommentContainer}>
        <h1
          className="stamp"
          style={{
            animationName: "stamp",
            color: "#78ceff",
            fontSize: "30px",
            animationDuration: "1s",
            animationIterationCount: "9999",
          }}
        >
          남는건 결국{" "}
          <span
            style={{
              fontSize: "45px",
              color: "#1daeff",
              fontWeight: "bold",
              textShadow: "3px 3px 3px #78ceff",
            }}
          >
            도장&nbsp;
          </span>
          밖에 없지~
        </h1>
        <div style={{ opacity: stack < 0 ? "0" : "1", transition: "0.8s" }}>
          <h1
            style={{
              opacity: stack < 5 ? "1" : "0",
              transition: stack < 5 ? "0.6s" : "0s",
            }}
          >
            추억을 찍고
          </h1>
          <h1
            style={{
              opacity: stack < 3 ? "1" : "0",
              transition: stack < 3 ? "0.6s" : "0s",
            }}
          >
            도장을 쌓아요 !
          </h1>
          <h1
            style={{
              opacity: stack < 1 ? "1" : "0",
              transition: stack < 1 ? "0.6s" : "0s",
            }}
          >
            추억을 돌아봐요 !
          </h1>
        </div>
      </div>

      <div className={styles.DeckContainer}>
        <Deck setStack={setStack} />
        <div className={styles.metContainer}>
          <img
            // className="moving"
            src="assets/direction.png"
            style={{
              width: "450px",
              height: "150px",
              animationDuration: "1s",
              animationIterationCount: "9999",
              // animationDirection: "left",
              // transition: "1s",
            }}
            alt=""
          />
        </div>
      </div>
    </div>
  );
}

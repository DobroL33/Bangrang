import React, { useState } from "react";
import { useSprings, animated, to as interpolate } from "@react-spring/web";
import { useDrag } from "react-use-gesture";

import styles from "./ThirdPage.module.css";

const cards = [
  "assets/images/제주.png",
  "assets/images/대전.png",
  "assets/images/서울.png",
  "assets/images/경주.png",
  "assets/images/부산.png",
  "assets/images/야경.png",
];

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

const stamping = (i: number) => {
  if (i !== 0) {
    console.log(`"${i}번째 스탬프 쾅"`);
    console.log(i);
  } else {
    console.log(`"${i}번째 스탬프 쾅"`);
    console.log("흠 다 모았군");
  }
};

const flying = window.innerWidth * 0.2;

function Deck() {
  const [gone] = useState(() => new Set());
  const [props, api] = useSprings(cards.length, (i) => ({
    ...to(i),
    from: from(i),
  }));

  const bind = useDrag(
    ({ args: [index], down, movement: [mx], direction: [xDir], velocity }) => {
      const trigger = velocity > 0.2; // If you flick hard enough it should trigger the card to fly out
      const dir = xDir < 0 ? -1 : 0; // Direction should either point left or right
      if (!down && trigger) gone.add(index); // If button/finger's up and trigger velocity is reached, we flag the card ready to fly out
      api.start((i) => {
        if (index !== i) return; // We're only interested in changing spring-data for the current spring
        const isGone = gone.has(index);
        const x = isGone
          ? (window.innerWidth - flying * 2) * dir
          : down
          ? mx
          : 1; // When a card is gone it flys out left or right, otherwise goes back to zero
        const rot = mx / 100 + (isGone ? dir * 10 * velocity : 0); // How much the card tilts, flicking it harder makes it rotate faster
        const scale = down ? 1.1 : 1; // Active cards lift up a bit
        isGone ? stamping(i) : null; // n번째
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
          {/* This is the card itself, we're binding our gesture to it (and inject its index so we know which is which) */}
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

export default function ThirdPage() {
  const [divOpacity, setDivOpacity] = useState(Array(6).fill(1));

  return (
    <div className={styles.Page}>
      <div
        className={styles.InfoContainer}
        style={{ marginLeft: flying / 2, position: "relative" }}
      >
        <img src="assets/images/Phone.png" alt="" />
        <div
          className={styles.infos}
          style={{ zIndex: 3, position: "absolute" }}
        >
          <div>야경도 다녀왔구열</div>
          <div>부산도 다녀왔구열</div>
          <div>경주도 다녀왔구열</div>
          <div>서울도 다녀왔구열</div>
          <div>대전도 다녀왔구열</div>
          <div>제주도 다녀왔구열</div>
        </div>
      </div>

      <div className={styles.DeckContainer}>
        <Deck />
      </div>
    </div>
  );
}

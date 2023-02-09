interface Props {
  currTime: Date;
  kr_curr: Date;
}

const useTranslateTIme = ({ currTime, kr_curr }: Props) => {
  let [currYear, currMonth, currDay, currHour, currMinute, currSecond] = [
    currTime.getFullYear(),
    currTime.getMonth() + 1,
    currTime.getDate(),
    currTime.getHours(),
    currTime.getMinutes(),
    currTime.getSeconds(),
  ];

  let [
    createYear,
    createMonth,
    createDay,
    createHour,
    createMinute,
    createSecond,
  ] = [
    kr_curr.getFullYear(),
    kr_curr.getMonth() + 1,
    kr_curr.getDate(),
    kr_curr.getHours(),
    kr_curr.getMinutes(),
    kr_curr.getSeconds(),
  ];

  let finalCreatedAt = "";

  if (
    currYear === createYear &&
    currMonth === createMonth &&
    currDay === createDay
  ) {
    if (createHour > 12) {
      if (createHour - 12 < 10 && createMinute < 10) {
        finalCreatedAt = `오후 0${createHour - 12}:0${createMinute}`;
      } else if (createHour - 12 < 10) {
        finalCreatedAt = `오후 0${createHour - 12}:${createMinute}`;
      } else if (createMinute < 10) {
        finalCreatedAt = `오후 ${createHour - 12}:0${createMinute}`;
      } else {
        finalCreatedAt = `오후 ${createHour - 12}:${createMinute}`;
      }
    } else {
      if (createHour < 10 && createMinute < 10) {
        finalCreatedAt = `오전 0${createHour}:0${createMinute}`;
      } else if (createHour < 10) {
        finalCreatedAt = `오전 0${createHour}:${createMinute}`;
      } else if (createMinute < 10) {
        finalCreatedAt = `오전 ${createHour}:0${createMinute}`;
      } else {
        finalCreatedAt = `오후 ${createHour}:${createMinute}`;
      }
    }
  } else if (
    currYear === createYear &&
    currMonth === createMonth &&
    currDay - createDay === 1
  ) {
    finalCreatedAt = `어제`;
  } else {
    finalCreatedAt = `${createYear}-${createMonth}-${createDay}`;
  }

  return finalCreatedAt;
};

export default useTranslateTIme;

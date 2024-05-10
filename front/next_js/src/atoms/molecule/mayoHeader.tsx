import { useEffect, useState } from "react";

// 시간을 포맷하는 함수
const formatDate = (date: Date): string => {
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const day = date.getDate().toString().padStart(2, '0');
  const dayOfWeek = ['일', '월', '화', '수', '목', '금', '토'][date.getDay()];
  const time = date.toLocaleTimeString('en-US', { hour12: false });
  return `${month}. ${day}. (${dayOfWeek}) ${time}`;
};  

export default function MayoHeader() {
    // 현재 날짜와 시간을 가져오기
    const [currentTime, setCurrentTime] = useState(new Date());
  
    useEffect(() => {
      const timerID = setInterval(() => setCurrentTime(new Date()), 1000);
      return () => clearInterval(timerID);
    }, []);

  return (
    <>
      <div className="flex rounded-tr-[2rem] rounded-tl-[2rem] w-[100%] h-[15rem] bg-black text-white text-7xl font-bold" >
      <div className="w-[50%] flex justify-center items-center" >
      <div className="w-[4rem] h-[4rem] bg-red rounded-full mr-[2rem]"></div>
        <div>영업 종료</div>
      </div>
      <div className="w-[50%] flex justify-center items-center">{formatDate(currentTime)}</div>
      </div>
    </>
  );
}

"use client";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

interface EndOrderDetailModalProps {
  newOrderId: string | null;
}

export default function EndOrderDetailModal(props: EndOrderDetailModalProps) {
  return (
    <div className=" absolute top-[20rem] left-[105rem] flex flex-col justify-center text-black text-5xl h-[90rem] w-[128rem] ">
      <div className="flex justify-center items-center font-bold h-[20rem] border border-black">
        <div className="w-[60rem]">메뉴 1개 총 7,000원</div>
      </div>
      <div className="flex justify-center">
        <div>
          <div className="h-[15rem]  border border-black m-[1rem] p-[2rem]">
            <div className="font-bold mb-[0.5rem]">
              요청사항 {props.newOrderId}
            </div>
            <div className="text-4xl ">
              <div className="mb-[0.5rem]">7시10분 방문</div>
              <div>일회용품 사용 여부: 일회용품 필요해요!</div>
            </div>
          </div>
          <div className="h-[60rem] w-[79rem]  border border-black m-[1rem] p-[2rem] ">
            <div className="font-bold  h-[8rem] ">주문내역</div>
            <div>
              <div className="mb-[5rem]">함박 촉촉 스테이크 1 7.000원</div>
              <div className="mb-[5rem]">함박 촉촉 스테이크 1 7.000원</div>
              <div className="mb-[5rem]">함박 촉촉 스테이크 1 7.000원</div>
            </div>
          </div>
        </div>
        <div className="h-[76rem] w-[50rem] border border-black m-[1rem] p-[2rem] flex flex-col items-center">
          <div className="h-[68rem] ">
            <div className="font-bold text-6xl mb-[1rem]">김지훈</div>
            <div className="mb-[2rem]">010-3570-7418</div>
            <div className="text-3xl mb-[3rem]">
              안심번호는 주문 접수 후 최대 3시간 동안 유효합니다.
            </div>
            <div className="mb-[3rem]">주문번호: qZysVx1HlLEaIZMX</div>
            <div className="mb-[3rem]">주문시간: 19:05</div>
            <div>픽업시간: 01/10 19:10</div>
          </div>
          <div className="flex justify-center items-center bg-[#F2BE22] border-black h-[5rem] w-[18rem] rounded-[1rem]">
            주문 완료
          </div>
        </div>
      </div>
    </div>
  );
}

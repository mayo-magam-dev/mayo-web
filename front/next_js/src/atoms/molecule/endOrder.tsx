"use client";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
interface endOrderProps {
  endOrderId: string;
  onClick: (newOrderId: string) => void;
}

export default function EndOrder(props: endOrderProps) {
  return (
    <div
      className="flex flex-col text-[#CFCFCF] justify-center text-4xl h-[14rem] pl-[2rem] bg-[#333439] cursor-pointer"
      onClick={() => props.onClick(props.endOrderId)}
    >
      <div className="flex items-center mb-[1rem]">
        <div>주문 메뉴: {props.endOrderId}</div>
        <div className="text-6xl font-bold">완료한 주문 메뉴</div>
      </div>
      <div>주문일시: 2024-01-10 / 19:05</div>
    </div>
  );
}

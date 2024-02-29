"use client";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
interface ProcessingOrderProps {
  processingOrderId: string;
  onClick: (newOrderId: string) => void;
}

export default function ProcessingOrder(props: ProcessingOrderProps) {
  const router = useRouter();

  return (
    <div
      className="flex flex-col text-[#CFCFCF] justify-center text-4xl h-[14rem] pl-[2rem] bg-[#333439]"
      onClick={() => props.onClick(props.processingOrderId)}
    >
      <div className="flex items-center mb-[1rem]">
        <div>주문 메뉴:</div>
        <div className="text-6xl font-bold">함박 촉촉 스테이크</div>
      </div>
      <div>주문일시: 2024-01-10 / 19:05</div>
    </div>
  );
}

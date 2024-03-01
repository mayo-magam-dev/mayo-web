"use client";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import ProcessingOrder from "./processingOrder";
import ProcessingOrderDetailModal from "../atom/ProcessingOrderDetailModal";
import { usePathname } from "next/navigation";
import IteminfoDetailModal from "../atom/magageIteminfoDetailModal";

interface endOrderProps {
  key: number;
  endOrderId: string;
  onClick: (newOrderId: string) => void;
}

export default function ManageMenu(props: endOrderProps) {
  const router = useRouter();
  const pathName: string = usePathname();
  let id: string | undefined;

  if (typeof pathName === "string") {
    id = pathName.split("/").pop();
  } else {
    // pathName이 undefined인 경우에 대한 처리
    id = undefined;
  }

  return (
    <div
      className="text-[#595D66] text-4xl m-[4rem] cursor-pointer"
      onClick={() => props.onClick(props.endOrderId)}
    >
      <div className="flex flex-col justify-center items-center h-[24rem] w-[24rem]  font-bold rounded-[4rem] border border-black">
        <div className="flex justify-center items-center w-[14rem] h-[8rem] rounded-[2rem] border border-black mb-[1rem]">
          사진
        </div>
        <div className="w-[14rem]">1. 함박 바삭 스테이크</div>
      </div>
    </div>
  );
}

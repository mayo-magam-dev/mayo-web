"use client";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import ProcessingOrder from "./processingOrder";
import ProcessingOrderDetailModal from "../atom/ProcessingOrderDetailModal";
import { usePathname } from "next/navigation";

export default function EnrollMenu() {
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
    <div className="flex  text-5xl pt-[10rem]">
      <div className=" w-[45rem] flex justify-center ">네모</div>
      <div className="w-[67rem] flex justify-center ">함박 바삭 스테이크</div>
      <div className="w-[40rem] flex justify-center ">수량</div>
    </div>
  );
}

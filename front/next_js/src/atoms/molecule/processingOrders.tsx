"use client";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import ProcessingOrder from "./processingOrder";
import ProcessingOrderDetailModal from "../atom/ProcessingOrderDetailModal";
import { usePathname } from "next/navigation";

export default function ProcessingOrders() {
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
    <div className="text-[#F0BD22]">
      <div className="flex items-center h-[8rem] text-7xl font-bold pl-[2rem] bg-[#25272B]">
        진행 1건
      </div>
      <div className="h-[49rem] overflow-y-auto">
        {Array.from({ length: 20 }, (_, index) => (
          <div
            onClick={() =>
              router.push("/mayo/processing/" + id + "/process/" + id)
            }
          >
            <ProcessingOrder key={index} processingOrderId={String(index)} />
          </div>
        ))}
      </div>
    </div>
  );
}

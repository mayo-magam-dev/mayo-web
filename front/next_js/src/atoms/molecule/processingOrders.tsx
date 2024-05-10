"use client";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import ProcessingOrder from "./processingOrder";
import ProcessingOrderDetailModal from "../atom/ProcessingOrderDetailModal";

export default function ProcessingOrders() {
  return (
    <div className="text-[#F0BD22]">
      <div className="flex items-center h-[8rem] text-7xl font-bold pl-[2rem] bg-[#25272B]">
        진행 1건
      </div>
      <div className="h-[49rem] overflow-y-auto">
        <ProcessingOrder processingOrderId={"1"} />
        <ProcessingOrder processingOrderId={"1"} />
        <ProcessingOrder processingOrderId={"1"} />
        <ProcessingOrder processingOrderId={"1"} />
        <ProcessingOrder processingOrderId={"1"} />
      </div>
    </div>
  );
}

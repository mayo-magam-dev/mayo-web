"use client";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import NewOrder from "./newOrder";
import ProcessingOrder from "./processingOrder";

export default function ProcessingOrders() {
  const router = useRouter();

  return (
    <div className="text-[#F0BD22]">
      <div className="flex items-center h-[8rem] text-7xl font-bold pl-[2rem] bg-[#25272B]">
        진행 1건
      </div>
      <div className="h-[49rem] overflow-y-auto">
        <ProcessingOrder />
        <ProcessingOrder />
        <ProcessingOrder />
        <ProcessingOrder />
        <ProcessingOrder />
        <ProcessingOrder />
        <ProcessingOrder />
      </div>
    </div>
  );
}

"use client";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import NewOrders from "./newOrders";
import ProcessingOrders from "./processingOrders";
import NewOrderDetailModal from "../atom/newOrderDetailModal";

export default function MayoProcessingMain() {
  const [selectedNewOrderId, setSelectedNewOrderId] = useState<string | null>(
    null
  );

  return (
    <div className="h-[90rem] text-6xl  bg-[#0E0E0E] w-[80rem]">
      <NewOrders />
      <ProcessingOrders />
    </div>
  );
}

"use client";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import NewOrder from "./newOrder";
import NewOrderDetailModal from "../atom/newOrderDetailModal";

export default function NewOrders() {
  const [selectedNewOrderId, setSelectedNewOrderId] = useState<string | null>(
    null
  );

  const handleNewOrderOpenModal = (newOrderId: string) => {
    setSelectedNewOrderId(newOrderId);
  };

  return (
    <div className="text-[#F0BD22] ">
      <div className="flex items-center h-[8rem] text-7xl font-bold pl-[2rem] bg-[#25272B]">
        신규 1건
      </div>
      <div className="h-[25rem] overflow-y-auto font-bold">
        <NewOrder newOrderId={"1"} onClick={handleNewOrderOpenModal} />
        <NewOrder newOrderId={"2"} onClick={handleNewOrderOpenModal} />
        <NewOrder newOrderId={"3"} onClick={handleNewOrderOpenModal} />
      </div>
      {selectedNewOrderId !== null && (
        <NewOrderDetailModal newOrderId={selectedNewOrderId} />
      )}
    </div>
  );
}

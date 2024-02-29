"use client";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import ProcessingOrder from "./processingOrder";
import ProcessingOrderDetailModal from "../atom/ProcessingOrderDetailModal";
import EndOrder from "./endOrder";
import EndOrderDetailModal from "../atom/endOrderDetailModal";

export default function EndOrders() {
  const [selectedNewOrderId, setSelectedNewOrderId] = useState<string | null>(
    null
  );

  // 모달 열기 함수
  const handleOpenModal = (newOrderId: string) => {
    setSelectedNewOrderId(newOrderId);
  };

  return (
    <div className="text-[#F0BD22]">
      <div className="flex items-center h-[8rem] text-7xl font-bold pl-[2rem] bg-[#25272B]">
        완료 1건
      </div>
      <div className="h-[82rem] overflow-y-auto">
        {Array.from({ length: 20 }, (_, index) => (
          <EndOrder
            key={index}
            endOrderId={index + ""}
            onClick={handleOpenModal}
          />
        ))}
      </div>
      {selectedNewOrderId !== null && (
        <EndOrderDetailModal newOrderId={selectedNewOrderId} />
      )}
    </div>
  );
}

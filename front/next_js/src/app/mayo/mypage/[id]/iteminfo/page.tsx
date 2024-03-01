"use client";
import EndOrderDetailModal from "@/atoms/atom/endOrderDetailModal";
import ManageIteminfoDetailModal from "@/atoms/atom/magageIteminfoDetailModal";
import IteminfoDetailModal from "@/atoms/atom/magageIteminfoDetailModal";
import NewOrderDetailModal from "@/atoms/atom/newOrderDetailModal";
import ManageMenu from "@/atoms/molecule/manageMenu";
// http://localhost:3000/mayo/processing/Asb0QBuQ9gmEyyWlSVhY
import MayoHeader from "@/atoms/molecule/mayoHeader";
import MayoProcessingMain from "@/atoms/molecule/mayoProcessingMain";
import NavigationLeft from "@/atoms/molecule/navigationLeft";
import NewOrder from "@/atoms/molecule/newOrders";
import { usePathname } from "next/navigation";
import { useState } from "react";

export default function MayoProcessing() {
  const pathName: string = usePathname();
  let id: string | undefined;
  if (typeof pathName === "string") {
    id = pathName.split("/").pop();
  } else {
    // pathName이 undefined인 경우에 대한 처리
    id = undefined;
  }
  const [selectedManageItemInfoId, setSelectedNewOrderId] = useState<
    string | null
  >(null);
  const [isManageItemDetailModalOpen, setIsManageItemDetailModalOpen] =
    useState(false);

  // 모달 열기 함수
  const handleOpenModal = (selectedManageItemInfoId: string) => {
    setSelectedNewOrderId(selectedManageItemInfoId);
  };

  return (
    <main className="rounded-[2rem] w-[95%] h-[100rem] m-[5rem] ">
      <header>
        <MayoHeader />
      </header>
      <div className="flex ">
        <NavigationLeft id={id} />

        <div className="flex h-[80rem] w-[200rem] border border-black overflow-y-auto flex-wrap m-[5rem]">
          <div className="text-[#595D66] text-4xl m-[4rem]">
            <div className="flex flex-col justify-center items-center h-[24rem] w-[24rem]  font-bold rounded-[4rem] border border-black">
              추가
            </div>
          </div>
          {Array.from({ length: 100 }, (_, index) => (
            <div onClick={() => setIsManageItemDetailModalOpen(true)}>
              <ManageMenu
                key={index}
                endOrderId={index + ""}
                onClick={handleOpenModal}
              />
            </div>
          ))}
        </div>
      </div>
      {isManageItemDetailModalOpen
        ? selectedManageItemInfoId !== null && (
            <ManageIteminfoDetailModal
              selectedManageItemInfoId={selectedManageItemInfoId}
              setIsManageItemDetailModalOpen={setIsManageItemDetailModalOpen}
            />
          )
        : null}
    </main>
  );
}

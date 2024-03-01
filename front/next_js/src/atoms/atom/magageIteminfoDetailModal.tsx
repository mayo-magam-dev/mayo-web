"use client";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import NewOrder from "../molecule/newOrder";

interface ManageIteminfoDetailModalProps {
  selectedManageItemInfoId: string | null;
  setIsManageItemDetailModalOpen: (isOpen: boolean) => void;
}

export default function ManageIteminfoDetailModal(
  props: ManageIteminfoDetailModalProps
) {
  const handleModal = () => {
    props.setIsManageItemDetailModalOpen(false);
  };

  return (
    <div
      className=" absolute top-[20rem] left-[105rem] flex flex-col justify-center text-black text-5xl h-[90rem] w-[128rem] "
      onClick={handleModal}
    >
      sadfsdf {props.selectedManageItemInfoId}
    </div>
  );
}

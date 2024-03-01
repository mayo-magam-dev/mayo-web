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

  const textFormParentDiv = "flex h-[6rem] justify-between items-center";
  const leftTextCss = "text-5xl  font-bold ";
  const rightTextForm =
    "flex justify-between items-center h-[5rem] w-[45rem] text-black bg-[#ffffff]";

  return (
    <div
      className="absolute flex justify-center items-center top-[0rem] left-[0rem] left-[5rem] w-[240rem] h-[120rem]  flex flex-col justify-center text-5xl  "
      onClick={handleModal}
    >
      <div
        className="w-[70rem] h-[90rem] rounded-[2rem] bg-[#363535] pt-[1rem]"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="text-[#ffffff] m-[2rem]">
          <div className="text-4xl">다른 화면을 누르시면 꺼집니다!</div>
          <div className="flex mb-[3rem]">
            <div className="flex flex-col justify-center w-[35rem]  font-bold text-7xl">
              상품 관리
            </div>
            <div className="flex justify-end text-black w-[35rem] font-bold">
              <div className="flex justify-center items-center mr-[3rem] h-[6rem] w-[11rem] rounded-[1rem]  bg-[#FEC264]">
                삭제
              </div>
              <div className="flex justify-center items-center h-[6rem] w-[11rem] rounded-[1rem]  bg-[#d5d5d3]">
                저장
              </div>
            </div>
          </div>
          <div className="mb-[4rem] h-[23rem] w-[66rem] bg-[#d5d5d3] rounded-[2rem]"></div>
          <div className={textFormParentDiv}>
            <div className={leftTextCss}>상품명</div>
            <div className={rightTextForm}>더블치즈 촉촉 함박스테이크</div>
          </div>
          <div className={textFormParentDiv}>
            <div className={leftTextCss}>상품설명</div>
            <div className={rightTextForm}>더블치즈 촉촉 함박스테이크</div>
          </div>
          <div className={textFormParentDiv}>
            <div className={leftTextCss}>할인 전 가격</div>
            <div className={rightTextForm}>더블치즈 촉촉 함박스테이크</div>
          </div>
          <div className={textFormParentDiv}>
            <div className={leftTextCss}>할인 후 가격</div>
            <div className={rightTextForm}>더블치즈 촉촉 함박스테이크</div>
          </div>
          <div className={textFormParentDiv}>
            <div className={leftTextCss}>할인율</div>
            <div className={rightTextForm}>더블치즈 촉촉 함박스테이크</div>
          </div>
          <div className={textFormParentDiv}>
            <div className={leftTextCss}>추가 전달 사항</div>
            <div className={rightTextForm}>더블치즈 촉촉 함박스테이크</div>
          </div>
          <div className={textFormParentDiv}>
            <div className={leftTextCss}>조리시간</div>
            <div className={rightTextForm}>더블치즈 촉촉 함박스테이크</div>
          </div>
        </div>
      </div>
    </div>
  );
}

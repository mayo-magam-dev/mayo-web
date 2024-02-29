"use client";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

type data = {
  id: string | undefined;
};

export default function NavigationLeft({ id }: data) {
  const router = useRouter();
  return (
    <div className="h-[90rem] w-[20rem] text-white text-6xl font-bold bg-blue rounded-bl-[2rem]">
      <div
        className="flex justify-center items-center h-[18%] bg-red"
        onClick={() => router.push("/mayo/processing/" + id)}
      >
        처리중
      </div>
      <div
        className="flex justify-center items-center h-[18%] bg-black"
        onClick={() => router.push("/mayo/end/" + id)}
      >
        완료
      </div>
      <div
        className="flex justify-center items-center h-[18%] bg-red"
        onClick={() => router.push("/mayo/enroll/" + id)}
      >
        등록
      </div>
      <div
        className="flex justify-center items-center h-[18%] bg-black"
        onClick={() => router.push("/mayo/mypage/" + id)}
      >
        마이페이지
      </div>
      <div
        className="flex justify-center items-center h-[18%] bg-red"
        onClick={() => router.push("/mayo/processing/" + id)}
      >
        설정
      </div>
      <div className="h-[10%]">사진</div>
    </div>
  );
}

import StompJs from "stompjs";
import SockJS from "sockjs-client";
import { useRecoilState } from "recoil";
import { chatListState } from "atoms/chat/chatAtom";

let stomp_client: StompJs.Client;
let chatSub: StompJs.Subscription;

interface Props {
  handleConnectSocket: (state: boolean) => void;
}

// socket 서버 연결
export const connectSocket = async ({ handleConnectSocket }: Props) => {
  if (typeof window !== "undefined") {
    const socket = new SockJS(`${process.env.NEXT_PUBLIC_API_URL}/ws/chat`);
    stomp_client = StompJs.over(socket);

    const Authorization = `Bearer ${localStorage.getItem("token")}`;
    const headers = {
      Authorization,
    };
    stomp_client.connect(
      headers,
      frame => {
        console.log(frame?.command);
        if (frame?.command === "CONNECTED") {
          handleConnectSocket(true);
        } else {
          handleConnectSocket(false);
        }
        stomp_client.heartbeat.outgoing = 0;
        stomp_client.heartbeat.incoming = 0;
      },
      err => console.log(err),
    );
  }
};

// 채팅방 목록 구독
export const subscribeRoomList = (memberId: number | null) => {
  const Authorization = `Bearer ${localStorage.getItem("token")}`;
  const headers = {
    Authorization,
  };

  stomp_client.subscribe(
    `/topic/alarm/member/${memberId}`,
    msg => {
      console.log("this: ", msg);
    },
    headers,
  );
};

interface Props2 {
  roomId: number;
  handleSocketData: (data: any) => void;
}

// 채팅방 구독
export const subscribeRoom = ({ roomId, handleSocketData }: Props2) => {
  const Authorization = `Bearer ${localStorage.getItem("token")}`;
  const headers = {
    Authorization,
  };

  chatSub = stomp_client.subscribe(
    `/topic/rooms/${roomId}`,
    msg => {
      handleSocketData(JSON.parse(msg.body));
    },
    headers,
  );
};

// 메세지전송
export const sendMsg = (roomId: number, content: string) => {
  const Authorization = `Bearer ${localStorage.getItem("token")}`;
  const headers = {
    Authorization,
  };

  const body = {
    chatRoomId: roomId,
    messageContent: content,
  };

  const memberId = localStorage.getItem("memberId");

  // 채팅방에 메시지 전송
  stomp_client.send(`/app/rooms`, headers, JSON.stringify(body));
  // memberId로 구독한 채팅목록에 메시지 전송
  stomp_client.send(
    `/topic/alarm/member/${memberId}}`,
    headers,
    JSON.stringify(body),
  );
};

export const unSubscribeRoom = () => {
  chatSub.unsubscribe();
};

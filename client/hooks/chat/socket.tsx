import StompJs from "stompjs";
import SockJS from "sockjs-client";
import { useRecoilState } from "recoil";
import { chatListState } from "atoms/chat/chatAtom";

let stomp_client: StompJs.Client;
let chatSub: StompJs.Subscription;
let chatRoomSub: StompJs.Subscription;

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
      },
      err => console.log(err),
    );
  }
};

// 채팅방 목록 구독
interface ChatRoomListProps {
  memberId: number;
  handleSubChatRoom: (date: any) => void;
}
export const subscribeRoomList = ({
  memberId,
  handleSubChatRoom,
}: ChatRoomListProps) => {
  const Authorization = `Bearer ${localStorage.getItem("token")}`;
  const headers = {
    Authorization,
  };

  chatRoomSub = stomp_client.subscribe(
    // `/topic/alarm/member/${memberId}`,
    `/topic/member/${memberId}`,
    msg => {
      console.log("flag");
      handleSubChatRoom(JSON.parse(msg.body));
    },
    headers,
  );
};

interface Props2 {
  roomIdNum: number;
  handleSocketData: (data: any) => void;
}

// 채팅방 구독
export const subscribeRoom = ({ roomIdNum, handleSocketData }: Props2) => {
  const Authorization = `Bearer ${localStorage.getItem("token")}`;
  const headers = {
    Authorization,
  };

  chatSub = stomp_client.subscribe(
    `/topic/rooms/${roomIdNum}`,
    msg => {
      handleSocketData(JSON.parse(msg.body));
    },
    headers,
  );
};

// 메세지전송
export const sendMsg = (
  roomId: number,
  content: string,
  receiverId: number,
) => {
  const Authorization = `Bearer ${localStorage.getItem("token")}`;
  const headers = {
    Authorization,
  };

  const body = {
    chatRoomId: roomId,
    senderId: Number(localStorage.getItem("memberId")),
    receiverId: receiverId,
    messageContent: content,
  };

  console.log(body);

  // 채팅방에 메시지 전송
  stomp_client.send(`/app/rooms`, headers, JSON.stringify(body));
};

export const unSubscribeRoom = () => {
  chatSub.unsubscribe();
};

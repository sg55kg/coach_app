import {ChatRoom, Message, type MessageDTO} from "$lib/classes/chat";
import type {ChatRoomDTO} from "$lib/classes/chat";
import {srGet, srPost} from "./helpers/serviceRequest";



export class ChatService {

    static createChatRoom = async (newChat: ChatRoom) => {
        const { data } = await srPost<ChatRoomDTO>('/api/chat-room', newChat)
        return ChatRoom.createFrom(data);
    }

    static deleteChatRoom = async (chatId: string) => {

    }

    static getChatRoomsByUser = async (userId: string) => {
        const { data } = await srGet<ChatRoomDTO[]>(`/api/chat-room/user/${userId}`)
        return data.map(r => ChatRoom.createFrom(r))
    }

    static getChatRoom = async (chatId: string) => {

    }

    static getNext20Messages = async (chatId: string, start: number, end: number) => {
        const { data } = await srGet<MessageDTO[]>(`/api/message/${chatId}?start=${start}&end=${end}`)
        return data.map(m => Message.createFrom(m))
    }

}
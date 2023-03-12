import {ChatRoom} from "$lib/classes/chat";
import type {ChatRoomDTO} from "$lib/classes/chat";
import {Socket} from "phoenix";


export class ChatService {

    static createChatRoom = async (newChat: ChatRoom) => {
        const roomRes = await fetch('/api/chat-room', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(newChat)
        })
        const chatRoom: ChatRoomDTO = await roomRes.json()

        return ChatRoom.createFrom(chatRoom);
    }

    static deleteChatRoom = async (chatId: string) => {

    }

    static getChatRoomsByUser = async (userId: string) => {
        const res = await fetch(`/api/chat-room/user/${userId}`, { method: 'GET' })

        const rooms: ChatRoomDTO[] = await res.json()
        return rooms.map(r => ChatRoom.createFrom(r))
    }

    static getChatRoom = async (chatId: string) => {

    }

}
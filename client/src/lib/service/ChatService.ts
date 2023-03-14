import {ChatRoom, Message, type MessageDTO} from "$lib/classes/chat";
import type {ChatRoomDTO} from "$lib/classes/chat";



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

    static getNext20Messages = async (chatId: string, start: number, end: number) => {
        const res = await fetch(`/api/message/${chatId}?start=${start}&end=${end}`, {
            method: 'GET'
        })
        console.log(res)
        const data: MessageDTO[] = await res.json()
        return data.map(m => Message.createFrom(m))
    }

}
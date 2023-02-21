import dayjs, {Dayjs} from "dayjs";
import type {UserDTO} from "$lib/classes/user";
import {User} from "$lib/classes/user";

export interface MessageDTO {
    id: string,
    sentAt: string,
    lastEdit: string,
    senderId: string,
    senderName: string,
    chatId: string,
    contents: string,
    replies: Array<MessageDTO>
}

export class Message {
    static createFrom(data: MessageDTO) {
        let message = new Message()

        message.id = data.id
        message.sentAt = dayjs(data.sentAt)
        message.lastEdit = dayjs(data.sentAt)
        message.senderId = data.senderId
        message.senderName = data.senderName
        message.chatId = data.chatId
        message.replies = data.replies.map(d => Message.createFrom(d))

        return message
    }

    id: string = ''
    sentAt: Dayjs = dayjs()
    lastEdit: Dayjs = dayjs()
    senderId: string = ''
    senderName: string = ''
    chatId: string = ''
    contents: string = ''
    replies: Message[] = []
}

export interface ChatRoomDTO {
    id: string,
    members: Array<UserDTO>,
    teamId: string,
    messages: Array<MessageDTO>
}

export class ChatRoom {
    static createFrom(data: ChatRoomDTO) {
        let chatRoom = new ChatRoom()

        chatRoom.id = data.id
        chatRoom.members = data.members.map(d => User.build(d))
        chatRoom.teamId = data.teamId
        chatRoom.messages = data.messages.map(d => Message.createFrom(d))

        return chatRoom
    }

    id: string = ''
    members: User[] = []
    teamId: string = ''
    messages: Message[] = []
}
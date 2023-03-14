import dayjs, {Dayjs} from "dayjs";

export interface MessageDTO {
    id: string,
    sentAt: string,
    updatedAt: string,
    sender: ChatMemberDTO,
    chatId: string,
    contents: string,
    replies: Array<MessageDTO>,
}

export class Message {
    static createFrom(data: MessageDTO) {
        let message = new Message()
        message.id = data.id
        message.sentAt = dayjs(data.sentAt)
        message.updatedAt = dayjs(data.updatedAt)
        message.sender = data.sender ? ChatMember.createFrom(data.sender) : new ChatMember()
        message.chatId = data.chatId
        message.replies = data.replies ? data.replies.map(d => Message.createFrom(d)) : []
        message.contents = data.contents

        return message
    }

    id: string = ''
    sentAt: Dayjs = dayjs()
    updatedAt: Dayjs = dayjs()
    sender: ChatMember = new ChatMember()
    chatId: string = ''
    contents: string = ''
    replies: Message[] = []
}

export interface ChatRoomDTO {
    id: string,
    members: Array<ChatMemberDTO>
    teamId: string,
    messages: Array<MessageDTO>,
    name: string,
    messageCount: number
}

export class ChatRoom {
    static createFrom(data: ChatRoomDTO) {
        let chatRoom = new ChatRoom()

        chatRoom.id = data.id
        chatRoom.members = data.members.filter(d => d !== undefined).map(d => d ?? ChatMember.createFrom(d))
        chatRoom.teamId = data.teamId
        chatRoom.messages = data.messages ?
            data.messages.filter(d => d !== undefined).map(d => Message.createFrom(d))
                .sort((a, b) => a.sentAt.valueOf() - b.sentAt.valueOf()) :
            []
        chatRoom.name = data.name
        chatRoom.messageCount = data.messageCount

        return chatRoom
    }

    id: string = ''
    members: ChatMember[] = []
    teamId: string = ''
    messages: Message[] = []
    name: string = ''
    messageCount: number = 0
}

export interface ChatMemberDTO {
    id: string,
    userId: string,
    chatId: string,
    username?: string,
    photoUrl?: string
}

export class ChatMember {
    static createFrom(data: ChatMemberDTO) {
        const member = new ChatMember()

        member.id = data.id
        member.userId = data.userId
        member.chatId = data.chatId
        member.username = data.username ? data.username : ''
        member.photoUrl = data.photoUrl ? data.photoUrl : ''

        return member
    }

    id: string = ''
    userId: string = ''
    chatId: string = ''
    username?: string = ''
    photoUrl?: string = ''
}
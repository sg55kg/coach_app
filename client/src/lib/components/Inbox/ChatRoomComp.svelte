<script lang="ts">
    import {isMobile, userDB} from "$lib/stores/authStore.js";
    import {Channel} from "phoenix";
    import {ChatMember, ChatRoom, Message} from "$lib/classes/chat";
    import dayjs from "dayjs";
    import {chatError, loadingChat, notifications} from "$lib/stores/chatStore";
    import {onMount} from "svelte";
    import MessageBubble from "$lib/components/Inbox/MessageBubble.svelte";
    import LoadingSpinner from "$lib/components/shared/loading/LoadingSpinner.svelte";
    import {ChatService} from "$lib/service/ChatService";
    import FaChevronLeft from 'svelte-icons/fa/FaChevronLeft.svelte'

    export let channel: Channel
    export let messages: Message[] = []
    export let groupId: string
    export let member: ChatMember
    export let chatRoom: ChatRoom
    export let selectedChatId: string

    let newMessageContent: string = ''
    let messagesContainer: HTMLDivElement
    let showContextMenu: boolean = false
    let fetchingNextPage: boolean = false

    let typingUsers: string[] = []

    const addMessage = async () => {
        $loadingChat = true

        const newMessage: Message = {
            chatId: groupId,
            contents: newMessageContent,
            sender: member,
            replies: [] as Message[],
            sentAt: dayjs(),
            updatedAt: dayjs(),
            id: ''
        } as Message

        channel.push('new:msg', newMessage)
            .receive('error', (reasons) => $chatError = 'Error: Your message could not be sent at this time')
            .receive('timeout', () => console.log('slow much?'))
            .receive('ok', () => chatRoom.messageCount = chatRoom.messageCount + 1)
        //messages.unshift(newMessage)
        newMessageContent = ''

        $loadingChat = false
    }
    $: channel?.on('new:msg', (msg) => {
        const message = Message.createFrom(msg)

        if (messages.findIndex(m => m.id === msg.id) === -1) {
            messages.push(message)
        }
        messages = messages.sort((a, b) => a.sentAt.valueOf() - b.sentAt.valueOf())
        setTimeout(() => {
            document.getElementById('message-container').scrollTop = document.getElementById('message-container').scrollHeight
        }, 50)
    })

    const handleScroll = (e: Event) => {
        const container = document.getElementById('message-container')
        const scrollTop = container.scrollTop

        if (scrollTop === 0) {
            fetchingNextPage = true

        }
    }

    const fetchPage = async () => {
        let end = chatRoom.messageCount - messages.length
        if (end < 1) {
            return fetchingNextPage = false
        }
        const scrollHeight = document.getElementById('message-container')?.scrollHeight
        const PAGE_SIZE = 20
        let start = end <= PAGE_SIZE ? 0 : end - PAGE_SIZE - 1
        end = end > PAGE_SIZE ? PAGE_SIZE : end - 1

        try {
            const res: Message[] = await ChatService.getNext20Messages(groupId, start, end)
            messages = [...res, ...messages]
            chatRoom.messages = [...res, ...chatRoom.messages]

            document.getElementById('message-container').scrollTo(0, scrollHeight)
        } catch (e) {
            $chatError = 'Could not retrieve messages'
        }
        fetchingNextPage = false
    }

    $: fetchingNextPage ? fetchPage() : null

    $: channel?.on('typing', res => {
        if (!typingUsers.includes(res.user) && res.user !== $userDB?.username) {
            typingUsers.push(res.user)
            typingUsers = typingUsers
        }
    })

    $: typingUsers.length > 0 ? setTimeout(() => {
        typingUsers.shift()
        typingUsers = typingUsers
    }, 2500) : null

    onMount(() => {
        document.getElementById('message-container').scrollTop = document.getElementById('message-container').scrollHeight + 20
        document.getElementById('message-container').addEventListener('scroll', handleScroll)
        $notifications = $notifications.filter(m => m.chatId !== groupId)
    })
</script>

<div class="flex flex-col h-[89.8vh] overflow-hidden lg:mt-[-1em] w-full lg:px-6 relative">
    {#if $isMobile}
        <div class="flex items-center h-12 w-screen bg-gray-200">
            <div class="w-5 h-5 mx-4" on:click={() => selectedChatId = ''}>
                <FaChevronLeft />
            </div>
            <h1 class="text-lg font-semibold text-center w-9/12">{chatRoom.name}</h1>
        </div>
    {/if}
    {#if fetchingNextPage}
        <div class="flex justify-center w-full">
            <LoadingSpinner width={8} height={8} />
        </div>
    {/if}
    <div class="flex flex-col p-2 overflow-scroll h-[84vh] lg:h-[93vh] lg:pb-12 w-full" id="message-container" bind:this={messagesContainer}>
        {#each messages as message, idx}
            <MessageBubble message={message} messages={messages} index={idx} />
        {/each}

    </div>

    <div class="absolute bottom-0 w-full bg-gray-200">
        {#if typingUsers.length > 0}
            <p class="text-textblue text-center mb-2">
                {typingUsers.length < 2 ? typingUsers[0] + ' is typing...' :
                    (typingUsers.length < 3 ? typingUsers.join(' and ') + ' are typing...' :
                        typingUsers.length + ' users are typing...')}
            </p>
        {/if}
        <div class="grid grid-cols-6 lg:grid-cols-12 w-full h-12 max-h-44 lg:h-6/12 mt-4">
            <div class="col-span-5 lg:col-span-11 mr-2 lg:mr-4">
                <textarea
                       bind:value={newMessageContent}
                       style="resize: none"
                       on:keydown={(e) => e.key === 'Enter' ? addMessage() : channel.push("typing", { user: $userDB.username })}
                       class="w-full p-3 h-12 max-h-44 bg-gray-400 rounded-xl focus:outline focus:outline-link focus:outline-2"
                ></textarea>
            </div>
            <div class="col-span-1">
                <button class="bg-purple-600 hover:bg-purple-700 font-semibold rounded-xl p-3 lg:px-5" on:click={addMessage}>
                    Send
                </button>
            </div>
        </div>
    </div>

</div>

<style>

</style>
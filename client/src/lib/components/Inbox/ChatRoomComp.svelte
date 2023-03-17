<script lang="ts">
    import {isMobile, userDB} from "$lib/stores/authStore.js";
    import {Channel} from "phoenix";
    import {ChatMember, ChatRoom, Message} from "$lib/classes/chat";
    import dayjs from "dayjs";
    import {chatError, loadingChat, notifications} from "$lib/stores/chatStore";
    import {onDestroy, onMount} from "svelte";
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

    const addMessage = async (e: Event) => {
        e.preventDefault()
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
        return false
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
    }, 2000) : null

    $: channel?.on('error', (err) => {
        $chatError = 'An unknown error has occurred'
    })

    onMount(() => {
        document.getElementById('message-container').scrollTop = document.getElementById('message-container').scrollHeight + 20
        document.getElementById('message-container').addEventListener('scroll', handleScroll)
        $notifications = $notifications.filter(m => m.chatId !== groupId)
    })

    //onDestroy(() => document.getElementById('message-container').removeEventListener('scroll', handleScroll))
</script>

<div class="flex flex-col h-[100vh] lg:h-[89.8vh] lg:pb-12 pb-4 overflow-hidden lg:mt-[-1em] w-full relative">
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
    <div class="flex flex-col p-2 overflow-scroll h-[84vh] lg:h-[93vh] pb-4 lg:pb-12 w-full lg:px-6" id="message-container" bind:this={messagesContainer}>
        {#each messages as message, idx}
            <MessageBubble message={message} messages={messages} index={idx} />
        {/each}
        {#if typingUsers.length > 0}
            <p class="text-textblue text-center">
                {typingUsers.length < 2 ? typingUsers[0] + ' is typing...' :
                    (typingUsers.length < 3 ? typingUsers.join(' and ') + ' are typing...' :
                        typingUsers.length + ' users are typing...')}
            </p>
        {/if}
    </div>

   <div class="flex lg:justify-center w-full bg-gray-200 absolute bottom-0"> <!-- class="grid grid-cols-6 lg:grid-cols-12 w-full h-24 items-center content-center max-h-44 mt-1 bg-gray-200 w-full">-->

        <div class="w-10/12 lg:w-11/12 lg:col-span-11 mr-2 lg:mr-4 flex justify-center items-center p-2">
                <textarea
                        bind:value={newMessageContent}
                        style="resize: none"
                        on:keydown={(e) => e.key === 'Enter' ? addMessage(e) : channel.push("typing", { user: $userDB.username })}
                        class="w-full p-3 h-12 max-h-44 bg-gray-400 rounded-xl focus:outline focus:outline-link focus:outline-2"
                ></textarea>
        </div>
        <div class="w-2/12 lg:w-1/12 flex justify-center items-center">
            <button class="bg-purple-600 hover:bg-purple-700 font-semibold rounded-xl p-3 lg:px-5" on:click={addMessage}>
                Send
            </button>
        </div>
    </div>
</div>

<style>

</style>
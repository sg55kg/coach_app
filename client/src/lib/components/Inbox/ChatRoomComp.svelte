<script lang="ts">
    import {userDB} from "$lib/stores/authStore.js";
    import {Channel} from "phoenix";
    import {ChatMember, Message} from "$lib/classes/chat";
    import dayjs from "dayjs";
    import {loadingChat} from "$lib/stores/chatStore";
    import {onMount} from "svelte";
    import MessageBubble from "$lib/components/Inbox/MessageBubble.svelte";

    export let channel: Channel
    export let messages: Message[] = []
    export let groupId: string
    export let member: ChatMember

    let newMessageContent: string = ''
    let messagesContainer: HTMLDivElement
    let showContextMenu: boolean = false

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
            .receive('error', (reasons) => console.log('flop', reasons))
            .receive('timeout', () => console.log('slow much?'))
        //messages.unshift(newMessage)
        newMessageContent = ''

        $loadingChat = false
    }
    $: channel?.on('new:msg', (msg) => {
        console.log('shout', msg)
        const message = Message.createFrom(msg)
        if (messages.findIndex(m => m.id === msg.id) === -1) {
            messages.push(message)
        }
        messages = messages.sort((a, b) => a.sentAt.valueOf() - b.sentAt.valueOf())
        setTimeout(() => {
            document.getElementById('message-container').scrollTop = document.getElementById('message-container').scrollHeight
        }, 50)
    })

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
    })
</script>

<div class="flex flex-col h-[90vh] overflow-hidden w-full px-6 relative">
    <div class="flex flex-col p-2 overflow-scroll h-[87vh] pb-12" id="message-container" bind:this={messagesContainer}>
        {#each messages as message, idx}
            <MessageBubble message={message} messages={messages} index={idx} />
        {/each}

    </div>

    <div class="absolute bottom-0 w-full bg-gray-100">
        {#if typingUsers.length > 0}
            <p class="text-textblue text-center mb-2">
                {typingUsers.length < 2 ? typingUsers[0] + ' is typing...' :
                    (typingUsers.length < 3 ? typingUsers.join(' and ') + ' are typing...' :
                        typingUsers.length + ' users are typing...')}
            </p>
        {/if}
        <div class="grid grid-cols-12 w-full h-6/12 mt-4">
            <div class="col-span-11 mr-4">
                <input type="text"
                       bind:value={newMessageContent}
                       on:keydown={(e) => e.key === 'Enter' ? addMessage() : channel.push("typing", { user: $userDB.username })}
                       class="w-full p-3 bg-gray-400 rounded focus:outline focus:outline-link focus:outline-2"
                >
            </div>
            <div class="col-span-1">
                <button class="bg-purple-600 hover:bg-purple-700 font-semibold rounded-xl p-3 px-5" on:click={addMessage}>
                    Send
                </button>
            </div>
        </div>
    </div>

</div>

<style>

</style>
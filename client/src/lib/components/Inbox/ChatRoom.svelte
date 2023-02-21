<script lang="ts">
    import {userDB} from "$lib/stores/authStore.js";
    import {Channel} from "phoenix";
    import {Message} from "$lib/classes/chat";
    import dayjs from "dayjs";
    import {loadingChat} from "$lib/stores/chatStore";

    export let channel: Channel
    export let messages: Message[] = [{ senderName: 'Test', contents: 'Test message', replies: [], senderId: 'test', chatId: 'abc', sentAt: dayjs(), lastEdit: dayjs(), id: '1' }]
    export let groupId: string
    let newMessageContent: string = ''

    const addMessage = async () => {
        $loadingChat = true

        const newMessage: Message = {
            senderName: $userDB.username,
            contents: newMessageContent,
            replies: [],
            senderId: $userDB.id,
            sentAt: dayjs(),
            lastEdit: dayjs(),
            id: ''
        } as Message
        messages.unshift(newMessage)
        channel.push('new:msg', newMessage)
            .receive('error', (reasons) => console.log('flop', reasons))
            .receive('timeout', () => console.log('slow much?'))
        messages = messages
        newMessageContent = ''

        $loadingChat = false
    }



</script>

<div class="flex flex-col h-full overflow-hidden w-full px-6">
    <div class="flex flex-col p-2 overflow-scroll h-96 min-h-96">
        {#each messages.reverse() as message}
            <div class="rounded-xl p-4 my-2 {message.senderName === $userDB.username ? 'self-end bg-gradient-to-t to-purple-500 from-purple-600' : 'self-start bg-gray-200'}">
                {message.contents}
            </div>
        {/each}
    </div>
    <div class="grid grid-cols-12 w-full h-6/12 mt-4">
        <div class="col-span-11 mr-4">
            <input type="text"
                   bind:value={newMessageContent}
                   on:keydown={(e) => e.key === 'Enter' ? addMessage() : null}
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

<style>

</style>
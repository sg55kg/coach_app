<script lang="ts">
    import {Message} from "$lib/classes/chat";
    import {userDB} from "$lib/stores/authStore.js";

    export let message: Message
    export let messages: Message[]
    export let index: number

    $: isSender = message.sender.userId === $userDB?.id
    $: isLastInGroup = index >= messages.length - 1 || messages[index+1]?.sender?.id !== message.sender.id
    $: isFirstInGroup = index === 0 || messages[index-1].sender.id !== message.sender.id
    $: showDate = index === 0 || message.sentAt.diff(messages[index-1]?.sentAt, 'minutes') > 20

    const handleContextMenu = (e: PointerEvent) => {
        e.preventDefault()
    }

</script>

{#if showDate}
    <p class="text-textblue self-center py-4">{message.sentAt.format('ddd MMM DD hh:mm A')}</p>
{/if}
{#if isFirstInGroup}
    <p class="text-textblue text-sm {isSender ? 'self-end mr-12' : 'self-start ml-12'}">{message.sender.username}</p>
{/if}
<div class="flex {isSender ? 'flex-row-reverse' : 'flex-row'}">
    <div class="flex items-center {isSender ? 'flex-row' : 'flex-row-reverse'}">
        <p on:contextmenu={(e) => handleContextMenu(e)}
           class="rounded-xl p-3 my-2 {message.sender.userId === $userDB.id ? 'bg-gradient-to-t to-purple-500 from-purple-600' : 'bg-gray-200'} max-w-[250px] lg:max-w-[685px]"
        >
            {message.contents}
        </p>
        {#if isLastInGroup}
            <img src={message.sender.photoUrl} alt={'Photo of ' + message.sender.username} class="rounded-full h-8 w-8 lg:h-10 lg:w-10 bg-gray-300 {isSender ? 'ml-2' : 'mr-2'}">
            {:else}
            <div class="w-8 h-8 lg:w-10 lg:h-10 {isSender ? 'ml-2' : 'mr-2'}"></div>
        {/if}
    </div>
</div>

<style></style>
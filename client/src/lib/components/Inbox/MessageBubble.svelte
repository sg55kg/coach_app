<script lang="ts">
    import {Message} from "$lib/classes/chat";
    import {userDB} from "$lib/stores/authStore.js";

    export let message: Message
    export let messages: Message[]
    export let index: number

    $: isSender = message.sender.userId === $userDB?.id
    $: isLastInGroup = index >= messages.length-1 || messages[index+1].sender.id !== message.sender.id
    $: isFirstInGroup = index === 0 || messages[index-1].sender.id !== message.sender.id

    const handleContextMenu = (e: PointerEvent) => {
        e.preventDefault()
    }

</script>

{#if isFirstInGroup}
    <p class="text-textblue self-center">{message.sentAt.format('ddd MMM DD hh:mm A')}</p>
    <p class="text-textblue text-sm {isSender ? 'self-end mr-12' : 'self-start ml-12'}">{message.sender.username}</p>
{/if}
<div class="flex {isSender ? 'flex-row-reverse' : 'flex-row'}">
    <div class="flex items-center {isSender ? 'flex-row' : 'flex-row-reverse'}">
        <p on:contextmenu={(e) => handleContextMenu(e)}
           class="rounded-xl p-3 my-2 {message.sender.userId === $userDB.id ? 'bg-gradient-to-t to-purple-500 from-purple-600' : 'bg-gray-200'} lg:max-w-[685px]"
        >
            {message.contents}
        </p>
        {#if isLastInGroup}
            <img src={message.sender.photoUrl} alt={'Photo of ' + message.sender.username} class="rounded-full h-10 w-10 bg-gray-300 {isSender ? 'ml-2' : 'mr-2'}">
            {:else}
            <div class="w-10 h-10 {isSender ? 'ml-2' : 'mr-2'}"></div>
        {/if}
    </div>
</div>

<style></style>
<script lang="ts">
    import { Message } from '$lib/classes/chat';
    import { isMobile, userDB } from '$lib/stores/authStore.js';

    export let message: Message;
    export let messages: Message[];
    export let index: number;

    $: isSender = message.sender.userId === $userDB?.id;
    $: isLastInGroup =
        index >= messages.length - 1 ||
        messages[index + 1]?.sender?.id !== message.sender.id;
    $: isFirstInGroup =
        index === 0 || messages[index - 1].sender.id !== message.sender.id;
    $: showDate =
        index === 0 ||
        message.sentAt.diff(messages[index - 1]?.sentAt, 'minutes') > 20;

    let showContext: boolean = false;
    // let showMobileContext: boolean = false

    const handleContextMenu = (e: PointerEvent) => {
        e.preventDefault();
    };

    // const handleMobileContextMenu = (e: PointerEvent) => {
    //     e.preventDefault()
    //     showMobileContext = true
    // }
</script>

{#if showDate}
    <p class="self-center py-4 text-textblue">
        {message.sentAt.format('ddd MMM DD hh:mm A')}
    </p>
{/if}
{#if isFirstInGroup}
    <p
        class="text-sm text-textblue {isSender
            ? 'mr-12 self-end'
            : 'ml-12 self-start'}"
    >
        {message.sender.username}
    </p>
{/if}
<div class="flex {isSender ? 'flex-row-reverse' : 'flex-row'}">
    <div class="flex items-center {isSender ? 'flex-row' : 'flex-row-reverse'}">
        <p
            on:contextmenu="{e => handleContextMenu(e)}"
            class="my-2 rounded-xl p-3 {message.sender.userId === $userDB.id
                ? 'bg-gradient-to-t from-purple-600 to-purple-500'
                : 'bg-gray-200'} max-w-[250px] lg:max-w-[685px]"
        >
            {message.contents}
        </p>
        {#if isLastInGroup}
            <img
                src="{message.sender.photoUrl}"
                alt="{'Photo of ' + message.sender.username}"
                class="h-8 w-8 rounded-full bg-gray-300 lg:h-10 lg:w-10 {isSender
                    ? 'ml-2'
                    : 'mr-2'}"
            />
        {:else}
            <div
                class="h-8 w-8 lg:h-10 lg:w-10 {isSender ? 'ml-2' : 'mr-2'}"
            ></div>
        {/if}
    </div>
</div>

<style>
    p {
        -webkit-user-select: none !important;
        -webkit-touch-callout: none !important;
    }
</style>

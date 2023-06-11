<script lang="ts">
    import { isMobile, userDB } from '$lib/stores/authStore.js';
    import { Channel } from 'phoenix';
    import { ChatMember, ChatRoom, Message } from '$lib/classes/chat';
    import dayjs from 'dayjs';
    import {
        chatError,
        loadingChat,
        notifications,
    } from '$lib/stores/chatStore';
    import { onDestroy, onMount } from 'svelte';
    import MessageBubble from '$lib/components/Inbox/MessageBubble.svelte';
    import LoadingSpinner from '$lib/components/shared/loading/LoadingSpinner.svelte';
    import { ChatService } from '$lib/service/ChatService';
    import FaChevronLeft from 'svelte-icons/fa/FaChevronLeft.svelte';
    import FaPhotoVideo from 'svelte-icons/fa/FaPhotoVideo.svelte';

    export let channel: Channel;
    export let messages: Message[] = [];
    export let groupId: string;
    export let member: ChatMember;
    export let chatRoom: ChatRoom;
    export let selectedChatId: string;

    let newMessageContent: string = '';
    let messagesContainer: HTMLDivElement;
    let showContextMenu: boolean = false;
    let fetchingNextPage: boolean = false;
    let fileUpload: HTMLInputElement;

    let typingUsers: string[] = [];

    const addMessage = async (e: Event) => {
        e.preventDefault();
        $loadingChat = true;

        const newMessage: Message = {
            chatId: groupId,
            contents: newMessageContent,
            sender: member,
            replies: [] as Message[],
            sentAt: dayjs(),
            updatedAt: dayjs(),
            id: '',
        } as Message;

        channel
            .push('new:msg', newMessage)
            .receive(
                'error',
                reasons =>
                    ($chatError =
                        'Error: Your message could not be sent at this time')
            )
            .receive('timeout', () => console.log('slow much?'))
            .receive(
                'ok',
                () => (chatRoom.messageCount = chatRoom.messageCount + 1)
            );
        //messages.unshift(newMessage)
        newMessageContent = '';

        $loadingChat = false;
        return false;
    };
    $: channel?.on('new:msg', msg => {
        const message = Message.createFrom(msg);

        if (messages.findIndex(m => m.id === msg.id) === -1) {
            messages.push(message);
        }
        messages = messages.sort(
            (a, b) => a.sentAt.valueOf() - b.sentAt.valueOf()
        );
        setTimeout(() => {
            document.getElementById('message-container').scrollTop =
                document.getElementById('message-container').scrollHeight;
        }, 50);
    });

    const handleScroll = (e: Event) => {
        const container = document.getElementById('message-container');
        const scrollTop = container.scrollTop;

        if (scrollTop === 0) {
            fetchingNextPage = true;
        }
    };

    const fetchPage = async () => {
        let end = chatRoom.messageCount - messages.length;
        if (end < 1) {
            return (fetchingNextPage = false);
        }
        const scrollHeight =
            document.getElementById('message-container')?.scrollHeight;
        const PAGE_SIZE = 20;
        let start = end <= PAGE_SIZE ? 0 : end - PAGE_SIZE - 1;
        end = end > PAGE_SIZE ? PAGE_SIZE : end - 1;

        try {
            const res: Message[] = await ChatService.getNext20Messages(
                groupId,
                start,
                end
            );
            messages = [...res, ...messages];
            chatRoom.messages = [...res, ...chatRoom.messages];

            document
                .getElementById('message-container')
                .scrollTo(0, scrollHeight);
        } catch (e) {
            $chatError = 'Could not retrieve messages';
        }
        fetchingNextPage = false;
    };

    const handleUpload = async (file: File) => {
        let reader = new FileReader();
        reader.addEventListener('load', () => {
            const payload = {
                binary: reader?.result?.split(',', 2)[1],
                filename: file.name,
            };
            channel
                .push('upload:media', payload)
                .receive(
                    'error',
                    () =>
                        ($chatError =
                            'Error, could not upload video at this time')
                )
                .receive('ok', res => console.log(res));
        });

        reader.readAsDataURL(file);
    };

    $: fetchingNextPage ? fetchPage() : null;

    $: channel?.on('typing', res => {
        if (!typingUsers.includes(res.user) && res.user !== $userDB?.username) {
            typingUsers.push(res.user);
            typingUsers = typingUsers;
        }
    });

    $: typingUsers.length > 0
        ? setTimeout(() => {
              typingUsers.shift();
              typingUsers = typingUsers;
          }, 2000)
        : null;

    $: channel?.on('error', err => {
        $chatError = 'An unknown error has occurred';
    });

    onMount(() => {
        document.getElementById('message-container').scrollTop =
            document.getElementById('message-container').scrollHeight + 20;
        document
            .getElementById('message-container')
            .addEventListener('scroll', handleScroll);
        $notifications = $notifications.filter(m => m.chatId !== groupId);
    });

    //onDestroy(() => document.getElementById('message-container').removeEventListener('scroll', handleScroll))
</script>

<div
    class="relative flex h-[100vh] w-full flex-col overflow-hidden pb-4 lg:mt-[-1em] lg:h-[89.8vh] lg:pb-12"
>
    {#if $isMobile}
        <div class="flex h-12 w-screen items-center bg-gray-200">
            <div class="mx-4 h-5 w-5" on:click="{() => (selectedChatId = '')}">
                <FaChevronLeft />
            </div>
            <h1 class="w-9/12 text-center text-lg font-semibold">
                {chatRoom.name}
            </h1>
        </div>
    {/if}
    {#if fetchingNextPage}
        <div class="flex w-full justify-center">
            <LoadingSpinner width="w-8" height="h-8" />
        </div>
    {/if}
    <div
        class="flex h-[84vh] w-full flex-col overflow-scroll p-2 pb-4 lg:h-[93vh] lg:px-6 lg:pb-12"
        id="message-container"
        bind:this="{messagesContainer}"
    >
        {#each messages as message, idx}
            <MessageBubble
                message="{message}"
                messages="{messages}"
                index="{idx}"
            />
        {/each}
        {#if typingUsers.length > 0}
            <p class="text-center text-textblue">
                {typingUsers.length < 2
                    ? typingUsers[0] + ' is typing...'
                    : typingUsers.length < 3
                    ? typingUsers.join(' and ') + ' are typing...'
                    : typingUsers.length + ' users are typing...'}
            </p>
        {/if}
    </div>

    <div class="absolute bottom-0 flex w-full bg-gray-200 lg:justify-center">
        <!-- class="grid grid-cols-6 lg:grid-cols-12 w-full h-24 items-center content-center max-h-44 mt-1 bg-gray-200 w-full">-->

        <div
            class="mr-2 flex w-10/12 items-center justify-center p-2 lg:col-span-11 lg:mr-1 lg:w-11/12"
        >
            <textarea
                bind:value="{newMessageContent}"
                style="resize: none"
                on:keydown="{e =>
                    e.key === 'Enter'
                        ? addMessage(e)
                        : channel.push('typing', { user: $userDB.username })}"
                class="h-12 max-h-44 w-full rounded-xl bg-gray-400 p-3 focus:outline focus:outline-2 focus:outline-link"
            ></textarea>
        </div>
        <div class="flex w-2/12 items-center justify-around lg:w-1/12">
            <!--            <button class="text-gray-500 mx-1 h-8" on:click={() => fileUpload.click()}>-->
            <!--                <FaPhotoVideo />-->
            <!--            </button>-->
            <button
                class="rounded-xl bg-purple-600 p-3 font-semibold hover:bg-purple-700 disabled:bg-purple-800 lg:px-3"
                disabled="{!newMessageContent}"
                on:click="{addMessage}"
            >
                Send
            </button>
            <input
                type="file"
                hidden
                bind:this="{fileUpload}"
                on:change="{e => handleUpload(e.target.files[0])}"
            />
        </div>
    </div>
</div>

<style>
</style>

<script lang="ts">
    import { isMobile, userDB } from '$lib/stores/authStore.js';
    import dayjs from 'dayjs';
    import AuthHeader from '$lib/components/AuthHeader.svelte';
    import ChatRoomComp from '$lib/components/Inbox/ChatRoomComp.svelte';
    import { onDestroy, onMount } from 'svelte';
    import { Channel, Socket } from 'phoenix';
    import { Team } from '$lib/classes/team';
    import type { ChatRoom, MessageDTO } from '$lib/classes/chat';
    import { ChatMember, Message } from '$lib/classes/chat';
    import { ChatService } from '$lib/service/ChatService';
    import MdClose from 'svelte-icons/md/MdClose.svelte';
    import {
        chatError,
        chatTimeout,
        notifications,
    } from '$lib/stores/chatStore.js';
    import LoadingSpinner from '$lib/components/shared/loading/LoadingSpinner.svelte';
    import type { PageServerData } from '../../../../.svelte-kit/types/src/routes/$types';
    import Toaster from '$lib/components/shared/layout/Toaster.svelte';

    export let data: PageServerData;
    let selectedChatId: string = '';

    let chatRooms: ChatRoom[] = [];

    $: messages = selectedChatId
        ? chatRooms.find(c => c.id === selectedChatId).messages
        : [];
    $: groupId = selectedChatId ? selectedChatId : '';
    $: selectedMember = selectedChatId
        ? $userDB?.members.find(m => m.chatId === selectedChatId)
        : new ChatMember();

    let channels: { channel: Channel; id: string }[] = [];
    let selectedTeamForCreate: string = '';
    let newChatName: string = '';
    $: selectedChannel = selectedChatId
        ? channels.find(c => c.id === selectedChatId)
        : null;
    $: selectedRoom = selectedChatId
        ? chatRooms.find(c => c.id === selectedChatId)
        : null;

    const connect = async () => {
        const rooms = await ChatService.getChatRoomsByUser($userDB.id);

        if (rooms) {
            chatRooms = rooms;
        }

        const socket = new Socket(import.meta.env.VITE_SOCKET_URL);
        socket.onOpen(() => console.log('connected'));
        socket.connect({});
        for (const member of $userDB?.members) {
            const chan = socket.channel(`room:${member.chatId}`, data.token);
            chan.join()
                .receive(
                    'error',
                    () => ($chatError = 'Could not join this chat')
                )
                .receive('ok', res =>
                    console.log('Access granted.' + JSON.stringify(res))
                )
                .receive(
                    'timeout',
                    () => ($chatTimeout = 'Connection timed out. Retrying...')
                );

            chan.on('new:msg', (msg: MessageDTO) => {
                if (selectedChatId === '' && msg.sender.userId !== $userDB.id) {
                    if (!$notifications.findIndex(m => m.id === msg.id) > -1) {
                        $notifications.push(Message.createFrom(msg));
                        $notifications = $notifications;
                        const room = chatRooms.find(r => r.id === msg.chatId);
                        room.messages = [
                            ...room.messages,
                            Message.createFrom(msg),
                        ];
                        room.messageCount = room.messageCount + 1;
                        chatRooms = chatRooms;
                    }
                }
            });
            channels.push({ channel: chan, id: member.chatId });
            console.log(chan);
        }

        channels = channels;
    };

    const createTeamChat = async () => {
        if (!selectedTeamForCreate || !$userDB?.coachData?.teams) return;
        const selectedTeam: Team = $userDB.coachData.teams.find(
            t => t.id === selectedTeamForCreate
        );
        const membersList = selectedTeam.athletes.map(a => a.id);

        const newChat = {
            members: membersList,
            teamId: selectedTeamForCreate,
            coachId: $userDB.coachData.id,
            name: newChatName,
        } as ChatRoom;
        try {
            const chatRoom: ChatRoom = await ChatService.createChatRoom(
                newChat
            );
            chatRooms = [...chatRooms, chatRoom];
            const member = chatRoom.members.find(m => m.userId === $userDB!.id);
            $userDB.members = [...$userDB.members, member];
        } catch (e) {
            console.log(e);
        }
    };

    $: $chatError ? setTimeout(() => ($chatError = ''), 3000) : null;
    $: $chatTimeout ? setTimeout(() => ($chatTimeout = ''), 3000) : null;

    onMount(() => {
        connect();
    });

    onDestroy(() => {
        channels.forEach(c => c.channel.leave());
    });
</script>

<AuthHeader />
<div class="h-11/12 grid grid-cols-10 overflow-hidden lg:mt-[-1em] lg:h-[94vh]">
    {#if ($isMobile && !selectedChatId) || !$isMobile}
        <div
            class="col-span-2 flex flex-col bg-gray-300 {$isMobile
                ? 'col-span-10'
                : ''}"
        >
            <div class="mb-4 flex justify-center p-4">
                <h1 class="text-xl font-bold text-textblue">Rooms</h1>
            </div>
            <div class="overflow-y-auto">
                {#each chatRooms as room}
                    <div
                        class="max-h-32 bg-gray-200 p-2 py-5 hover:cursor-pointer hover:bg-gray-400"
                        on:click="{() => (selectedChatId = room.id)}"
                    >
                        <div class="flex w-full justify-between">
                            <h4 class="mb-2 text-lg">{room.name}</h4>
                            {#if room.messages.length > 0}
                                <h4>
                                    {dayjs().diff(
                                        room.messages[room.messages.length - 1]
                                            .sentAt,
                                        'days'
                                    )
                                        ? dayjs().diff(
                                              room.messages[
                                                  room.messages.length - 1
                                              ].sentAt,
                                              'days'
                                          ) + 'd'
                                        : dayjs().diff(
                                              room.messages[
                                                  room.messages.length - 1
                                              ].sentAt,
                                              'hours'
                                          )
                                        ? dayjs().diff(
                                              room.messages[
                                                  room.messages.length - 1
                                              ].sentAt,
                                              'hours'
                                          ) + 'h'
                                        : dayjs().diff(
                                              room.messages[
                                                  room.messages.length - 1
                                              ].sentAt,
                                              'minutes'
                                          ) + 'm'}
                                </h4>
                            {/if}
                        </div>
                        <div class="flex w-11/12 flex-nowrap justify-between">
                            <small
                                class="truncate-ellipsis w-11/12 overflow-hidden text-textblue"
                            >
                                {room.messages.length > 0
                                    ? room.messages[room.messages.length - 1]
                                          .sender.username +
                                      ': ' +
                                      room.messages[room.messages.length - 1]
                                          .contents
                                    : 'No messages'}
                            </small>
                            {#if $notifications.findIndex(n => n.chatId === room.id) > -1}
                                <div
                                    class="h-4 w-4 self-center rounded-full bg-sky-300"
                                ></div>
                            {/if}
                        </div>
                    </div>
                {/each}
                {#if $isMobile}
                    <div class="col-span-8 flex flex-col items-center">
                        <div
                            class="flex flex-col rounded-xl bg-gray-300 p-6 px-10 shadow-lg"
                        >
                            <h2
                                class="m-4 mb-8 text-xl font-semibold text-textblue"
                            >
                                Start a group chat with your team
                            </h2>
                            <select
                                class="bg-gray-100 p-2"
                                on:change="{e =>
                                    (selectedTeamForCreate = e.target.value)}"
                            >
                                <option value="">Select a team...</option>
                                {#each $userDB.coachData.teams.filter(t => chatRooms.findIndex(c => c.teamId === t.id) < 0) as team}
                                    <option value="{team.id}"
                                        >{team.name}</option
                                    >
                                {/each}
                            </select>
                            <input
                                class="my-2 bg-gray-100 p-2 text-yellow-lt placeholder:text-textgray"
                                type="text"
                                placeholder="Enter chat name"
                                bind:value="{newChatName}"
                            />
                            <button
                                class="my-4 rounded bg-yellow p-2 text-black hover:bg-yellow-shade disabled:bg-gray-200"
                                on:click="{createTeamChat}"
                                disabled="{!selectedTeamForCreate ||
                                    !newChatName}"
                            >
                                Get Started
                            </button>
                        </div>
                    </div>
                {/if}
            </div>
        </div>
    {/if}
    {#if $userDB?.coachData?.teams?.length > 0 && !selectedChatId && !$isMobile}
        <div class="col-span-8 flex h-full flex-col items-center">
            <div
                class="m-auto flex flex-col rounded-xl bg-gray-300 p-6 px-10 shadow-lg"
            >
                <h2 class="m-4 mb-8 text-xl font-semibold text-textblue">
                    Start a group chat with your team
                </h2>
                <select
                    class="bg-gray-100 p-2"
                    on:change="{e => (selectedTeamForCreate = e.target.value)}"
                >
                    <option value="">Select a team...</option>
                    {#each $userDB.coachData.teams.filter(t => chatRooms.findIndex(c => c.teamId === t.id) < 0) as team}
                        <option value="{team.id}">{team.name}</option>
                    {/each}
                </select>
                <input
                    class="my-2 bg-gray-100 p-2 text-yellow-lt placeholder:text-textgray"
                    type="text"
                    placeholder="Enter chat name"
                    bind:value="{newChatName}"
                />
                <button
                    class="my-4 rounded bg-yellow p-2 text-black hover:bg-yellow-shade disabled:bg-gray-200"
                    on:click="{createTeamChat}"
                    disabled="{!selectedTeamForCreate || !newChatName}"
                >
                    Get Started
                </button>
            </div>
        </div>
    {:else if !selectedChatId}
        <div
            class="col-span-8 flex h-screen flex-col items-center justify-center"
        >
            <h2 class="text-xl font-semibold">Select a room to get started</h2>
        </div>
    {/if}
    {#if selectedChatId && selectedChannel}
        <div
            class="flex flex-col {$isMobile
                ? 'w-[100vw]'
                : 'col-span-8'} h-screen w-full items-center self-end lg:h-screen lg:justify-center"
        >
            <ChatRoomComp
                channel="{selectedChannel.channel}"
                bind:messages="{messages}"
                groupId="{groupId}"
                member="{selectedMember}"
                bind:chatRoom="{selectedRoom}"
                bind:selectedChatId="{selectedChatId}"
            />
        </div>
    {/if}
</div>
{#if $chatError}
    <Toaster type="error" bind:message="{$chatError}" />
{/if}
{#if $chatTimeout}
    <Toaster type="info" bind:message="{$chatTimeout}" spinner="{true}" />
{/if}

<style></style>

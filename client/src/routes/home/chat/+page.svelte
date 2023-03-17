<script lang="ts">

    import {isMobile, userDB} from "$lib/stores/authStore.js";
import dayjs from "dayjs";
import AuthHeader from "$lib/components/AuthHeader.svelte";
import ChatRoomComp from "$lib/components/Inbox/ChatRoomComp.svelte";
    import {onDestroy, onMount} from "svelte";
import {Channel, Socket} from 'phoenix'
import {Team} from "$lib/classes/team";
import type {ChatRoom, MessageDTO} from "$lib/classes/chat";
import {ChatMember, Message} from "$lib/classes/chat";
import {ChatService} from "$lib/service/ChatService";
import MdClose from 'svelte-icons/md/MdClose.svelte'
import {chatError, chatTimeout, notifications} from "$lib/stores/chatStore.js";
import LoadingSpinner from "$lib/components/shared/loading/LoadingSpinner.svelte";


let selectedChatId: string = ''

let chatRooms: ChatRoom[] = []

$: messages = selectedChatId ? chatRooms.find(c => c.id === selectedChatId).messages : []
$: groupId = selectedChatId ? selectedChatId : ''
$: selectedMember = selectedChatId ? $userDB?.members.find(m => m.chatId === selectedChatId) : new ChatMember()


let channels: { channel: Channel, id: string }[] = []
let selectedTeamForCreate: string = ''
let newChatName: string = ''
$: selectedChannel = selectedChatId ? channels.find(c => c.id === selectedChatId) : null
$: selectedRoom = selectedChatId ? chatRooms.find(c => c.id === selectedChatId) : null

const connect = async () => {
    const rooms = await ChatService.getChatRoomsByUser($userDB.id)

    if (rooms) {
        console.log(rooms)
        chatRooms = rooms
    }

    const socket = new Socket(import.meta.env.VITE_SOCKET_URL)
    socket.onOpen(() => console.log('connected'))
    socket.connect({})
    for (const member of $userDB?.members) {
        const chan = socket.channel(`room:${member.chatId}`)
        chan.join()
            .receive('error', () => $chatError = 'Could not join this chat')
            .receive('ok', (res) => console.log('Access granted.' + JSON.stringify(res)))
            .receive('timeout', () => $chatTimeout = 'Connection timed out. Retrying...')

        chan.on('new:msg', (msg: MessageDTO) => {
            if (selectedChatId === '' && msg.sender.userId !== $userDB.id) {
                if (!$notifications.findIndex(m => m.id === msg.id) > -1) {
                    $notifications.push(Message.createFrom(msg))
                    $notifications = $notifications
                    const room = chatRooms.find(r => r.id === msg.chatId)
                    room.messages = [...room.messages, Message.createFrom(msg)]
                    room.messageCount = room.messageCount + 1
                    chatRooms = chatRooms
                }
            }
        })
        channels.push({ channel: chan, id: member.chatId })
        console.log(chan)
    }

    channels = channels
}

const createTeamChat = async () => {
    if (!selectedTeamForCreate || !$userDB?.coachData?.teams) return
    const selectedTeam: Team = $userDB.coachData.teams.find(t => t.id === selectedTeamForCreate)
    const membersList = selectedTeam.athletes.map(a => a.id)

    const newChat = {
        members: membersList,
        teamId: selectedTeamForCreate,
        coachId: $userDB.coachData.id,
        name: newChatName
    } as ChatRoom
    try {
        const chatRoom: ChatRoom = await ChatService.createChatRoom(newChat)
        chatRooms = [...chatRooms, chatRoom]
        const member = chatRoom.members.find(m => m.userId === $userDB!.id)
        $userDB.members = [...$userDB.members, member]
    } catch (e) {
        console.log(e)
    }
}

$: $chatError ? setTimeout(() => $chatError = '', 3000) : null
$: $chatTimeout ? setTimeout(() => $chatTimeout = '', 3000) : null

onMount(() => {
    connect()
})

    onDestroy(() => {
        channels.forEach(c => c.channel.leave())
    })

</script>

<AuthHeader />
<div class="grid grid-cols-10 h-11/12 lg:h-[94vh] overflow-hidden lg:mt-[-1em]">
    {#if ($isMobile && !selectedChatId) || !$isMobile}
    <div class="flex flex-col col-span-2 bg-gray-300 {$isMobile ? 'col-span-10' : ''}">
        <div class="flex justify-center mb-4 p-4">
            <h1 class="text-xl text-textblue font-bold">Rooms</h1>
        </div>
       <div class="overflow-y-auto">
           {#each chatRooms as room}
               <div class="p-2 py-5 bg-gray-200 hover:bg-gray-400 hover:cursor-pointer max-h-32" on:click={() => selectedChatId = room.id}>
                   <div class="flex w-full justify-between">
                       <h4 class="text-lg mb-2">{room.name}</h4>
                       {#if room.messages.length > 0}
                           <h4>
                               {dayjs().diff(room.messages[room.messages.length-1].sentAt, 'days') ?
                                dayjs().diff(room.messages[room.messages.length-1].sentAt, 'days') + 'd' :
                                   (dayjs().diff(room.messages[room.messages.length-1].sentAt, 'hours') ?
                                    dayjs().diff(room.messages[room.messages.length-1].sentAt, 'hours') + 'h' :
                                    dayjs().diff(room.messages[room.messages.length-1].sentAt, 'minutes') + 'm')}
                           </h4>
                       {/if}
                   </div>
                   <div class="flex justify-between flex-nowrap w-11/12">
                       <small class="truncate-ellipsis overflow-hidden text-textblue w-11/12">
                           {room.messages.length > 0 ?
                               room.messages[room.messages.length-1].sender.username + ": " + room.messages[room.messages.length-1].contents :
                               'No messages'
                           }
                       </small>
                       {#if $notifications.findIndex(n => n.chatId === room.id) > -1}
                           <div class="rounded-full h-4 w-4 bg-sky-300 self-center"></div>
                       {/if}
                   </div>
               </div>
           {/each}
           {#if $isMobile}
               <div class="flex flex-col col-span-8 items-center">
                   <div class="flex flex-col bg-gray-300 rounded-xl shadow-lg p-6 px-10">
                       <h2 class="text-xl font-semibold text-textblue m-4 mb-8">Start a group chat with your team</h2>
                       <select class="bg-gray-100 p-2" on:change={(e) => selectedTeamForCreate = e.target.value}>
                           <option value="">Select a team...</option>
                           {#each $userDB.coachData.teams.filter(t => chatRooms.findIndex(c => c.teamId === t.id) < 0) as team}
                               <option value={team.id}>{team.name}</option>
                           {/each}
                       </select>
                       <input class="bg-gray-100 p-2 my-2 text-yellow-lt placeholder:text-textgray"
                              type="text"
                              placeholder="Enter chat name"
                              bind:value={newChatName}
                       >
                       <button class="bg-yellow p-2 rounded text-black my-4 hover:bg-yellow-shade disabled:bg-gray-200"
                               on:click={createTeamChat}
                               disabled={!selectedTeamForCreate || !newChatName}
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
        <div class="flex flex-col col-span-8 items-center h-full">
            <div class="flex flex-col m-auto bg-gray-300 rounded-xl shadow-lg p-6 px-10">
                <h2 class="text-xl font-semibold text-textblue m-4 mb-8">Start a group chat with your team</h2>
                <select class="bg-gray-100 p-2" on:change={(e) => selectedTeamForCreate = e.target.value}>
                    <option value="">Select a team...</option>
                    {#each $userDB.coachData.teams.filter(t => chatRooms.findIndex(c => c.teamId === t.id) < 0) as team}
                        <option value={team.id}>{team.name}</option>
                    {/each}
                </select>
                <input class="bg-gray-100 p-2 my-2 text-yellow-lt placeholder:text-textgray"
                       type="text"
                       placeholder="Enter chat name"
                       bind:value={newChatName}
                >
                <button class="bg-yellow p-2 rounded text-black my-4 hover:bg-yellow-shade disabled:bg-gray-200"
                        on:click={createTeamChat}
                        disabled={!selectedTeamForCreate || !newChatName}
                >
                    Get Started
                </button>
            </div>
        </div>
    {:else if !selectedChatId}
        <div class="flex flex-col justify-center items-center h-screen col-span-8">
            <h2 class="text-xl font-semibold">Select a room to get started</h2>
        </div>
    {/if}
    {#if selectedChatId && selectedChannel}
        <div class="flex flex-col {$isMobile ? 'w-[100vw]' : 'col-span-8'} lg:h-screen self-end h-screen items-center lg:justify-center w-full">
            <ChatRoomComp channel={selectedChannel.channel}
                          bind:messages={messages}
                          groupId={groupId}
                          member={selectedMember}
                          bind:chatRoom={selectedRoom}
                          bind:selectedChatId={selectedChatId}
            />
        </div>
    {/if}
</div>
{#if $chatError}
    <div class="sticky bottom-5 left-10 z-10 text-red border-l-4 border-l-red-shade bg-gray-200 shadow-2xl shadow-black p-4 w-9/12 lg:w-4/12 flex justify-between items-center">
        {$chatError}
        <button class="h-8 w-8 hover:bg-gray-400 rounded-full hover:text-red-shade p-1" on:click={() => $chatError = ''}>
            <MdClose />
        </button>
    </div>
{/if}
{#if $chatTimeout}
    <div class="sticky bottom-5 left-10 z-10 text-sky-500 border-l-4 border-l-sky-500 bg-gray-200 shadow-2xl shadow-black p-4 w-8/12 lg:w-4/12 flex justify-between items-center">
        <div class="flex items-center">
            <p class="mr-3">{$chatTimeout}</p>
            <LoadingSpinner />
        </div>
        <button class="h-8 w-8 hover:bg-gray-400 rounded-full hover:text-red-shade p-1" on:click={() => $chatTimeout = ''}>
            <MdClose />
        </button>
    </div>
{/if}

<style></style>
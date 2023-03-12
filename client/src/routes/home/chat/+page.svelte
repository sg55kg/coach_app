<script lang="ts">

import {userDB} from "$lib/stores/authStore.js";
import dayjs from "dayjs";
import AuthHeader from "$lib/components/AuthHeader.svelte";
import ChatRoomComp from "$lib/components/Inbox/ChatRoomComp.svelte";
import {onMount} from "svelte";
import {Channel, Socket} from 'phoenix'
import {Team} from "$lib/classes/team";
import type {ChatRoom, ChatRoomDTO} from "$lib/classes/chat";
import {ChatMember} from "$lib/classes/chat";
import {ChatService} from "$lib/service/ChatService";

let selectedChatId: string = ''

let chatRooms: ChatRoom[] = []

$: messages = selectedChatId ? chatRooms.find(c => c.id === selectedChatId).messages : []
$: groupId = selectedChatId ? selectedChatId : ''
$: selectedMember = selectedChatId ? $userDB?.members.find(m => m.chatId === selectedChatId) : new ChatMember()


let channels: { channel: Channel, id: string }[] = []
let selectedTeamForCreate: string = ''
let newChatName: string = ''
$: selectedChannel = selectedChatId ? channels.find(c => c.id === selectedChatId) : null

const connect = async () => {
    const rooms = await ChatService.getChatRoomsByUser($userDB.id)

    if (rooms) {
        console.log(rooms)
        chatRooms = rooms
    }

    const socket = new Socket('ws://localhost:4000/socket')
    socket.onOpen(() => console.log('connected'))
    socket.connect({})
    for (const member of $userDB?.members) {
        const chan = socket.channel(`room:${member.chatId}`)
        chan.join()
            .receive('error', () => console.log('Access denied.'))
            .receive('ok', (res) => console.log('Access granted.' + JSON.stringify(res)))
            .receive('timeout', () => console.log('Must be MongoDB.'))
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

onMount(() => {
    connect()
})

</script>

<AuthHeader />
<div class="grid grid-cols-10 h-[92vh] overflow-hidden mt-[-1em]">
    <div class="flex flex-col col-span-2 bg-gray-300 ">
        <div class="flex justify-center mb-4 p-4">
            <h1 class="text-xl text-textblue font-bold">Rooms</h1>
        </div>
       <div class="overflow-y-auto">
           {#each chatRooms as room}
               <div class="p-2 bg-gray-200 hover:bg-gray-400 hover:cursor-pointer" on:click={() => selectedChatId = room.id}>
                   <div class="flex w-full justify-between">
                       <h4 class="text-lg mb-2">{room.name}</h4>
                       {#if room.messages.length > 0}
                           <h4>{dayjs().subtract(room.messages[0].sentAt).get('minutes')}m</h4>
                       {/if}
                   </div>
                   <div class="flex flex-nowrap">
                       <small class="truncate-ellipsis overflow-hidden text-textblue">
                           {room.messages.length > 0 ?
                               room.messages[0].sender.username + ": " + room.messages[0].contents :
                               'No messages'
                           }
                       </small>
                   </div>
               </div>
           {/each}
       </div>
    </div>
    {#if $userDB?.coachData?.teams?.length > 0 && !selectedChatId && $userDB.members.length < 1}
        <div class="flex flex-col col-span-8 items-center h-full">
            <div class="flex flex-col m-auto bg-gray-300 rounded-xl shadow-lg p-6 px-10">
                <h2 class="text-xl font-semibold text-textblue m-4 mb-8">Start a group chat with your team</h2>
                <select class="bg-gray-100 p-2" on:change={(e) => selectedTeamForCreate = e.target.value}>
                    <option value="">Select a team...</option>
                    {#each $userDB.coachData.teams as team}
                        <option value={team.id}>{team.name}</option>
                    {/each}
                </select>
                <input class="bg-gray-100 p-2 my-2 text-yellow-lt placeholder:text-textgray"
                       type="text"
                       placeholder="Enter chat name"
                       bind:value={newChatName}
                >
                <button class="bg-yellow p-2 rounded text-black my-4 hover:bg-yellow-shade" on:click={createTeamChat}>
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
        <div class="flex flex-col col-span-8 items-center justify-center w-full">
            <ChatRoomComp channel={selectedChannel.channel} bind:messages={messages} groupId={groupId} member={selectedMember} />
        </div>
    {/if}
</div>

<style></style>
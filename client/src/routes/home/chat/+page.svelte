<script lang="ts">

import {userDB} from "$lib/stores/authStore.js";
import dayjs from "dayjs";
import AuthHeader from "$lib/components/AuthHeader.svelte";
import ChatRoom from "$lib/components/Inbox/ChatRoom.svelte";
import {onMount} from "svelte";
import {Socket} from 'phoenix'
import {Team} from "$lib/classes/team";

let selectedChatId: string = ''

let chatRooms: any[] = [
    {
        id: 'abc',
        name: 'Dollar Lift Club',
        messages: [
            {
                senderName: 'Sam',
                contents: 'This is a test message to see how this looks in the side nav with a lot of words and hopefully trail off and not get too crowded',
                sentAt: dayjs('2023-02-06')
            }
        ]
    }
]
$: messages = selectedChatId ? chatRooms.find(c => c.id === selectedChatId).messages : []
$: groupId = selectedChatId ? selectedChatId : ''
let channel
let selectedTeamForCreate: string = ''

const connect = async () => {
    const socket = new Socket('ws://localhost:4000/socket')
    socket.onOpen(() => console.log('connected'))
    socket.connect({})
    const chan = socket.channel('room:chat_room')
    chan.join()
        .receive('error', () => console.log('Access denied.'))
        .receive('ok', (res) => console.log('Access granted.' + JSON.stringify(res)))
        .receive('timeout', () => console.log('Must be MongoDB.'))

    chan.on('new:msg', msg => {
        const group = chatRooms.find(c => c.id === msg.groupId)
        group.messages.unshift({ senderName: msg.user, content: msg.body })
        // if (messages && msg.user !== $userDB?.username) {
        //     messages.push({ senderName: msg.user, content: msg.body })
        // }
        console.log('New message from server', msg)
    })

    channel = chan
}

const createTeamChat = async () => {
    if (!selectedTeamForCreate || !$userDB?.coachData?.teams) return
    const selectedTeam: Team = $userDB.coachData.teams.find(t => t.id === selectedTeamForCreate)
    const membersList = selectedTeam.athletes.map(a => a.id)

    const newChat = {
        members: membersList,
        teamId: selectedTeamForCreate,
        coachId: $userDB.coachData.id
    }
    try {
        const roomRes = await fetch('/api/chat-room', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(newChat)
        })
        console.log(roomRes)
    } catch (e) {
        console.log(e)
    }
}

onMount(() => {
    connect()
})

</script>

<AuthHeader />
<div class="grid grid-cols-10 h-screen overflow-hidden mt-[-1em]">
    <div class="flex flex-col col-span-2 bg-gray-300 ">
        <div class="flex justify-center mb-4 p-4">
            <h1 class="text-xl text-textblue font-bold">Rooms</h1>
        </div>
       <div class="overflow-y-auto">
           {#each chatRooms as room}
               <div class="p-2 bg-gray-200 hover:bg-gray-400 hover:cursor-pointer" on:click={() => selectedChatId = room.id}>
                   <div class="flex w-full justify-between">
                       <h4 class="text-lg mb-2">{room.name}</h4>
                       <h4>{dayjs().subtract(room.messages[room.messages.length-1].sentAt).get('minutes')}m</h4>
                   </div>
                   <div class="flex flex-nowrap">
                       <small class="truncate-ellipsis overflow-hidden text-textblue">
                           {room.messages[0]?.contents}
                       </small>
                   </div>
               </div>
           {/each}
       </div>
    </div>
    {#if $userDB?.coachData?.teams?.length > 0 && !selectedChatId}
        <div class="flex flex-col col-span-8 items-center h-screen">
            <div class="flex flex-col m-auto bg-gray-300 rounded-xl shadow-lg p-6 px-10">
                <h2 class="text-xl font-semibold text-textblue m-4 mb-8">Start a group chat with your team</h2>
                <select class="bg-gray-100 p-2" on:change={(e) => selectedTeamForCreate = e.target.value}>
                    <option value="">Select a team...</option>
                    {#each $userDB.coachData.teams as team}
                        <option value={team.id}>{team.name}</option>
                    {/each}
                </select>
                <button class="bg-yellow p-2 rounded text-black my-4 hover:bg-yellow-shade" on:click={createTeamChat}>
                    Get Started
                </button>
            </div>
        </div>
    {/if}
    {#if selectedChatId}
        <div class="flex flex-col col-span-8 items-center justify-center w-full">
            <ChatRoom channel={channel} messages={messages} groupId={groupId} />
        </div>
    {/if}
</div>

<style></style>
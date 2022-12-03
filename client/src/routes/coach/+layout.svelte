<script lang="ts">
    import type {PageData} from "./$types";
    import {userId} from "../../lib/stores/authStore";

    export let data: PageData
    const { userIdData } = data
    if($userId === '') {
        userId.set(userIdData)
    }
    console.log(data)
</script>

<nav class="top-0 flex justify-around bg-yellow-400 rounded-b-full py-2">
    <button class="hover:bg-yellow-500"><a href={`/coach/${$userId}`}>All Programs</a></button>
    <p>My Teams</p>
    <p>Chat</p>
</nav>
<slot />

<style>

</style>
<script lang="ts">
    import {auth0Client, user} from "../../lib/stores/authStore";
    import {onMount} from "svelte";
    import UserService from "../../lib/service/userService";


    onMount(async () => {

    })
</script>
<!-- TODO: Either remove home page or redirect to here -->
<section class="flex flex-col">
    <h1 class="text-xl text-center m-10 uppercase tracking-widest text-textgray font-semibold">Welcome</h1>
    <div class="flex flex-row self-center justify-around w-5/12">
        <a class="text-gray-200 bg-yellow rounded-xl p-2 font-bold text-center text-sm w-5/12 hover:bg-yellow-shade"
           href="/home/coach">
            Coach Dashboard
        </a>
        <a class="text-gray-200 bg-yellow rounded-xl p-2 font-bold text-center text-sm w-5/12 hover:bg-yellow-shade"
           href="/home/athlete">
            Athlete Dashboard
        </a>
    </div>
</section>

<style>

</style>
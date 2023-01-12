<script lang="ts">
    import {onMount} from "svelte";
    import {userDB} from "$lib/stores/authStore";
    import {goto} from "$app/navigation";


    onMount(async () => {

    })
</script>
<!-- TODO: Either remove home page or redirect to here -->
<section class="flex flex-col">
    <h1 class="text-3xl text-center m-10 uppercase tracking-widest text-textgray font-semibold">Welcome</h1>
    <div class="flex flex-col lg:flex-row self-center lg:justify-around w-5/12">
        <a class="text-gray-200 my-2 lg:my-0 bg-yellow rounded p-2 font-bold text-center text-sm lg:w-5/12 hover:bg-yellow-shade"
           href="/home/coach">
            Coach Dashboard
        </a>
        <a class="text-gray-200 my-2 lg:my-0 bg-yellow rounded p-2 font-bold text-center text-sm lg:w-5/12 hover:bg-yellow-shade"
           href="/home/athlete">
            Athlete Dashboard
        </a>
    </div>
</section>

<style>

</style>
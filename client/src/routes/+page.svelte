

<script lang="ts">
	import {auth0Client, isAuthenticated} from '$lib/stores/authStore'
	import UserService from "../lib/service/userService";
	import {goto} from '$app/navigation'

	const login = async () => {
		if (!$auth0Client) return
		await UserService.loginWithPopUp($auth0Client)
		await goto('/home')
	}

	const logout = async () => {
		if (!$auth0Client) return
		await UserService.logout($auth0Client)
	}

</script>

<svelte:head>
	<title>Home</title>
	<meta name="description" content="Coachable app" />
</svelte:head>

<section class="flex flex-col">
	{#if !$isAuthenticated}
		<h1 class="text-3xl">Hey Coach!</h1>
		<h1 class="text-2xl my-4 tracking-wider">Manage multiple athletes across several teams and stay organized. Track athletes' progress and make smarter choices when writing programs.</h1>
		<a href="/register">
			<button class="text-gray-200 bg-yellow rounded p-2 font-bold text-center hover:bg-yellow-shade m-5">
				Get Started
			</button>
		</a>
		<small>Already have an account?</small>

			<button class="tracking-wider font-semibold hover:text-yellow-lt" on:click={login}>
				Login
			</button>


	{:else }
		<h1 class="text-xl text-center m-10 uppercase tracking-widest text-textgray font-semibold">Welcome</h1>
		<div class="flex flex-row justify-self-center justify-around w-5/12">
			<a class="text-gray-200 bg-yellow rounded-xl p-2 font-bold text-center text-sm w-5/12 hover:bg-yellow-shade"
			   href="/home/coach">
				Coach Dashboard
			</a>
			<a class="text-gray-200 bg-yellow rounded-xl p-2 font-bold text-center text-sm w-5/12 hover:bg-yellow-shade"
			   href="/home/athlete">
				Athlete Dashboard
			</a>
		</div>

	{/if}
</section>

<style>
	section {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		flex: 0.6;
	}

	h1 {
		width: 100%;
	}

	.welcome {
		display: block;
		position: relative;
		width: 100%;
		height: 0;
		padding: 0 0 calc(100% * 495 / 2048) 0;
	}

	.welcome img {
		position: absolute;
		width: 100%;
		height: 100%;
		top: 0;
		display: block;
	}
</style>

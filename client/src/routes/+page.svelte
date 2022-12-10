

<script lang="ts">
	import {auth0Client, isAuthenticated} from '$lib/stores/authStore'
	import UserService from "../lib/service/userService";
	import {goto} from '$app/navigation'

	const login = async () => {
		await UserService.loginWithPopUp($auth0Client)
		await goto('/home')
	}

	const logout = async () => {
		await UserService.logout($auth0Client)
	}

</script>

<svelte:head>
	<title>Home</title>
	<meta name="description" content="Coachable app" />
</svelte:head>

<section>
	{#if !$isAuthenticated}
		<h1>Not logged in</h1>
		<a href="/register">
			<button>
				Get Started
			</button>
		</a>
		<small>Already have an account?</small>

			<button on:click={login}>
				Login
			</button>


	{:else }
		<h1 class="text-xl text-center m-10 uppercase tracking-widest text-textgray font-semibold">Welcome</h1>
		<button on:click={logout}>Logout</button>
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

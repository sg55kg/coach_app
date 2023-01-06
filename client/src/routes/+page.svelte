<script lang="ts">
	import {auth0Client, isAuthenticated} from '$lib/stores/authStore'
	import UserService from "../lib/service/userService";
	import {goto} from '$app/navigation'

	let popupOpen: boolean = false

	const login = async () => {
		if (!$auth0Client) return

		popupOpen = true
		try {
			await UserService.loginWithPopUp($auth0Client)
			await goto('/home')
		} catch (e) {
			console.log(e)
		}
		popupOpen = false
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

<section class="flex flex-col w-screen">

		<div class="w-full px-16">
			<h1 class="text-5xl font-bold tracking-wider">Connect with your Team</h1>
			<div class="w-6/12">
				<h1 class="text-xl text-wrap w-2 my-4 tracking-wider">
					Track all of your athletes' progress and stay organized across multiple teams. Use athlete feedback and data from your programs to make better decisions. Write better programs with Coachable.
				</h1>
			</div>
			<div class="flex flex-col text-center justify-center items-start m-2 mt-10">
				{#if !$isAuthenticated}
					<button on:click={login} class="text-gray-200 bg-yellow rounded p-4 px-6 font-bold text-center hover:bg-yellow-shade mx-2 mt-5 mb-2">
						Get Started
					</button>
					<div class="flex flex-col items-start">
						<small>Already have an account?</small>
						<button class="tracking-wider font-semibold hover:text-yellow-lt self-center" on:click={login}>
							Login
						</button>
					</div>
				{:else }
					<a href="/home" class="text-gray-200 bg-yellow rounded p-4 px-6 font-bold text-center hover:bg-yellow-shade mx-2 mt-5 mb-2">
						Go To Home
					</a>
				{/if}
			</div>
		</div>
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

<template>
    <div class="post-container">
        <PostCard />
        <PostCard />
        <PostCard />
        <PostCard />
        <PostCard />
        <PostCard />
        <PostCard />
    </div>
</template>

<script lang="ts" setup>
import PostCard from './PostCard.vue';
</script>

<style scoped>
.post-container {
    outline: 0; 
    width: 100%;
    display: flex;
    flex-wrap: wrap;
}
</style>

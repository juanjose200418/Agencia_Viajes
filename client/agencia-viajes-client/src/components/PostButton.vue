<template>
    <div>
        <button class="post-btn">Access</button>
    </div>
</template>

<script lang="ts" setup>
</script>

<style scoped>
.post-btn {
    padding: 12px 24px; /* Adjusted padding for better button shape */
    background-color: #e74c3c; /* A softer red */
    color: #fff; /* White text for contrast */
    border: none; /* No border */
    border-radius: 8px; /* Rounded corners */
    cursor: pointer; /* Pointer on hover */
    font-size: 1em; /* Standard text size */
    font-weight: bold; /* Bolder text */
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* Subtle shadow for depth */
    transition: background-color 0.3s, box-shadow 0.3s; /* Smooth transition on hover */
}

.post-btn:hover {
    background-color: #c0392b; /* Darker shade of red on hover */
    color: #fff; /* Keep white text for contrast */
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2); /* Larger shadow on hover */
    transform: scale(1.05); /* Slightly enlarge the button on hover */
}
</style>

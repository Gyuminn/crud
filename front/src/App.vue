<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'

</script>

<template>
  <el-header>
      <el-menu mode="horizontal" router>
        <el-menu-item index="/">게시판</el-menu-item>
        <el-menu-item index="/write">글 작성</el-menu-item>
      </el-menu>
  </el-header>

  <RouterView />
</template>

<style>
</style>
<template>
    <div class="charts-choose">
        <div class="search-box">
            <el-input
                v-model="searchParam"
                placeholder="输入后回车搜索"
                @input="inputEvent"
                @keyup.enter="initData"
            ></el-input>
        </div>
        <div class="form-dragger-widget">
            <template v-if="chartsList.length">
                <div :draggable="true" v-for="(element, index) in chartsList" :key="index" class="edit-item" @drag="startMoveEvent(element, $event)" @dragend="endMoveEvent(element, $event)">
                    <span class="draggable-name"><EllipsisTooltip class="label-name-text" :label="element.chartName" /></span>
                    <span class="draggable-type">{{element.typeName}}</span>
                    <el-button class="preview-chart" type="primary" link @click="previewChatEvent(element)">预览</el-button>
                </div>
            </template>
            <template v-else>
                <EmptyPage></EmptyPage>
            </template>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { ref, defineEmits, nextTick, defineProps } from 'vue'
import EmptyPage from '../empty-page/index.vue'
import EllipsisTooltip from '../ellipsis-tooltip/ellipsis-tooltip.vue'

const props = defineProps(['chartsList'])
const emit = defineEmits([ 'endMoveEvent', 'startMoveEvent', 'getChartListEvent', 'previewChatEvent'])

const guid = function() {
    function S4() {
        return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
    }
    return (S4() + S4() + '-' + S4() + '-' + S4() + '-' + S4() + '-' +S4() + S4() +S4());
}
const searchParam = ref('')

const onMove = (e: any) => {
    return true
}

function startMoveEvent(e: any, event: any) {
    e.i = guid()
    e.uuid = guid()
    emit('startMoveEvent', e)
}
function endMoveEvent(e: any, event: any) {
    e.i = guid()
    e.uuid = guid()
    emit('endMoveEvent', e)
}

function inputEvent(e: string) {
    if (e === '') {
        initData()
    }
}
function previewChatEvent(e: any) {
    emit('previewChatEvent', e)
}
function initData() {
    emit('getChartListEvent', searchParam.value)
}
</script>

<style lang="scss">
.charts-choose {
    height: 100%;
    min-width: 220px;
    border-left: 1px solid var(--el-border-color);
    border-right: 1px solid var(--el-border-color);
    background-color: #ffffff;
    .search-box {
        padding: 12px 0;
        box-sizing: border-box;
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 100%;
        border-bottom: 1px solid getCssVar('border-color');

        .el-input {
            margin: 0 12px;
            width: 100%;
            .el-input__wrapper {
                border-radius: 10px;
            }
        }
    }
    .form-dragger-widget {
        position: relative;
        height: calc(100vh - 114px);

        .edit-item {
            height: 50px;
            font-size: 12px;
            display: flex;
            flex-direction: column;
            justify-content: space-around;
            padding: 4px 12px;
            box-sizing: border-box;
            border: 1px solid #d5d5d5;
            border-radius: 10px;
            cursor: grab;
            margin: 10px 12px 0;
            box-shadow: getCssVar('box-shadow', 'lighter');
            transition: all 0.3s linear;
            position: relative;

            &:hover {
                transform: scale(1.03);
                transition: all 0.3s linear;
                border-color: getCssVar('color', 'primary');
                box-shadow: 0 0 8px 0 getCssVar('color', 'primary');
            }
            &:active {
                cursor: grabbing;
                &::after {
                    opacity: 0;
                }
            }
            .draggable-type {
                color: #7573d1;
                line-height: normal;
            }
            .draggable-name {
                max-width: 110px;
            }
            .preview-chart {
                position: absolute;
                right: 16px;
                top: 15px;
            }
        }
    }
}
</style>

<style>
::webkit-drag-shadow {
    color: #fff;
    background: #000;
    opacity: 0.5;
    border: 2px dashed #fff;
    text-align: center;
    font: bold 12px/24px 'Arial';
    color: red !important;
    border-color: red !important;
}
</style>
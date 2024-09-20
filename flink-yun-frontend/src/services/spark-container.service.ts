import { http } from '@/utils/http'
interface SerchParams {
  page: number;
  pageSize: number;
  searchKeyWord: string;
}

// flink容器-查询
export function GetSparkContainerList(params: SerchParams): Promise<any> {
  return http.request({
    method: 'post',
    url: '/vip/container/pageContainer',
    params: params
  })
}

// flink容器-添加
export function AddSparkContainerData(params: any): Promise<any> {
  return http.request({
    method: 'post',
    url: '/vip/container/addContainer',
    params: params
  })
}

// flink容器-更新
export function UpdateSparkContainerData(params: any): Promise<any> {
  return http.request({
    method: 'post',
    url: '/vip/container/updateContainer',
    params: params
  })
}

// flink容器-检测
export function ChecSparkContainerkData(params: any): Promise<any> {
  return http.request({
    method: 'post',
    url: '/vip/container/checkContainer',
    params: params
  })
}

// flink容器-删除
export function DeleteSparkContainerkData(params: any): Promise<any> {
  return http.request({
    method: 'post',
    url: '/vip/container/deleteContainer',
    params: params
  })
}

// flink容器-启动
export function StartSparkContainerkData(params: any): Promise<any> {
  return http.request({
    method: 'post',
    url: '/vip/container/startContainer',
    params: params
  })
}

// flink容器-停止
export function StopSparkContainerkData(params: any): Promise<any> {
  return http.request({
    method: 'post',
    url: '/vip/container/stopContainer',
    params: params
  })
}

// flink容器-获取日志信息
export function GetSparkContainerkDetail(params: any): Promise<any> {
  return http.request({
    method: 'post',
    url: '/vip/container/getContainer',
    params: params
  })
}

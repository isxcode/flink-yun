<template>

  <div class="footer">

    <div class="content">
      <div class="left">
        <img class="logo-img" src="https://isxcode.oss-cn-shanghai.aliyuncs.com/zhiliuyun/web-img/logo.jpg" alt=""/>
        <div class="logo-name">
          至爻数据
        </div>
        <img class="wechat-img" src="https://isxcode.oss-cn-shanghai.aliyuncs.com/zhiyao/qrcode_for_12.jpg" alt=""/>
        <div class="wechat-name">
          微信公众号
        </div>
      </div>
      <div class="right">
        <div class="column">
          <div class="title">产品</div>
          <div class="link" @click="clickzhiqingyun">至轻云</div>
          <div class="link" @click="clickZhiliuyun">至流云</div>
          <div class="link" @click="clickZhihuiyun">至慧云</div>
          <div class="link" @click="clickZhishiyun">至视云</div>
          <div class="link" @click="clickZhishuyun">至数云</div>
        </div>
        <div class="column">
          <div class="title">社区</div>
          <div class="link" @click="clickGithub">Github</div>
          <div class="link" @click="clickGitee">Gitee</div>
          <div class="link" @click="clickSlack">Slack</div>
          <div class="link" @click="clickDiscord">Discord</div>
        </div>
        <div class="column">
          <div class="title">资源</div>
          <div class="link" @click="clickDocs">产品手册</div>
          <div class="link" @click="clickBlogs">博客</div>
          <div class="link" @click="clickDockerHub">Docker Hub</div>
          <div class="link" @click="downloadPackage">安装包下载</div>
          <div class="link" @click="downloadLicense">许可证下载</div>
        </div>
        <div class="column">
          <div class="title">关于</div>
          <div class="link" @click="aboutUs">至爻数据</div>
          <div class="link" @click="joinUs">加入我们</div>
          <div class="link" @click="clickGithubProject">开发进度</div>
        </div>
        <div class="column">
          <div class="title">联系我们</div>
          <div class="link">电话: 400-8848-170</div>
          <div class="link">邮箱: hello@isxcode.com</div>
          <div class="link">地址: 上海市徐汇区中山西路2020号501室</div>
        </div>
      </div>
    </div>
  </div>

  <div class="org-info">
    <div class="content">
      <div class="link">公司: 上海至爻数据有限公司</div>
      <div class="link">电话: 400-8848-170</div>
      <div class="link">邮箱: hello@isxcode.com</div>
      <div class="link">地址: 上海市徐汇区中山西路2020号501室</div>
    </div>
  </div>

  <div class="footer-copyright">
    <div class="content">
      <div class="equal-columns">
        <div>Copyright © 2023-2024 至爻数据</div>
        <div class="icp-click" @click="clickIcp">沪ICP备2023031895号-1</div>
        <div class="copyright-div">版权所有 上海至爻数据有限公司</div>
      </div>
    </div>
  </div>

  <div class="fixed-footer" @click="callPhoneNumber">
    <div class="content">
      <SvgIcon class="phone-svg" name="phone"></SvgIcon>
      <p class="phone-text"><a>电话咨询</a></p>
    </div>
  </div>
</template>

<style lang="scss" scoped>

$primary-color: #7573d1;

.footer {
  font-family: "阿里巴巴普惠体 2.0 45 Light", sans-serif;
  padding-top: 40px;

  .content {
    width: 1200px;
    height: 300px;
    margin: auto;
    display: flex;

    .left {
      width: 300px;

      .logo-img {
        margin: auto;
        width: 80px;
      }

      .logo-name {
        margin-top: 10px;
        width: 300px;
        text-align: center;
        font-family: "阿里妈妈数黑体 Bold", sans-serif;
        font-size: 18px;
      }

      .wechat-img {
        margin: 20px auto auto;
        width: 120px;
        height: 120px;
      }

      .wechat-name {
        width: 300px;
        text-align: center;
        font-size: 10px;
        color: grey;
      }
    }

    .right {
      margin-left: 120px;
      width: 780px;
      display: flex;

      .column {
        display: flex;
        flex-direction: column;
        margin-right: 40px;

        .title {
          width: 75px;
          font-size: 18px;
          font-weight: bold;
        }

        .link {
          cursor: pointer;
          margin-top: 22px;
          font-size: 14px;
        }
      }
    }
  }

}

.footer-copyright {
  font-family: "阿里巴巴普惠体 2.0 45 Light", sans-serif;
  width: 100%;
  border-top: lavender solid 1px;

  .content {
    font-size: 12px;
    height: 30px;
    color: grey;
    width: 1200px;
    margin: auto;
    display: flex;
    align-items: center;
    justify-content: center;

    .equal-columns {
      .icp-click {
        cursor: pointer;
      }

      display: flex;
      flex: 1;
      justify-content: space-between;
    }
  }
}

.fixed-footer {
  display: none;
}

.org-info{
  display: none;
}

@media (max-width: 768px) {

  .fixed-footer {
    display: block;
    position: fixed;
    z-index: 999;
    bottom: 0;
    left: 0;
    width: 100%;
    height: 50px;
    line-height: 50px;
    text-align: center;
    background: #f5f3fe;

    .content {
      width: 130px;
      display: flex;
      align-items: center;
      margin: 0 auto;

      .phone-svg {
        width: 24px;
        height: 24px;
      }

      .phone-text {
        margin-left: 10px;
        font-size: 1.25rem;
        color: $primary-color;
      }
    }
  }

  .footer {
    padding-top: 40px;

    .content {
      width: 300px;
      height: 340px;
      margin: auto;
      display: flex;

      .left {
        width: 300px;

        .logo-img {
          margin: auto;
          width: 100px;
        }

        .logo-name {
          margin-top: 10px;
          width: 300px;
          text-align: center;
          font-family: "阿里妈妈数黑体 Bold", sans-serif;
          font-size: 28px;
        }

        .wechat-img {
          margin: 20px auto auto;
          width: 160px;
          height: 160px;
        }

        .wechat-name {
          width: 300px;
          text-align: center;
          font-size: 12px;
          color: grey;
        }
      }

      .right {
        display: none;

        .column {
          display: flex;
          flex-direction: column;
          margin-right: 40px;

          .title {
            width: 75px;
            font-size: 18px;
            font-weight: bold;
          }

          .link {
            margin-top: 22px;
            font-size: 14px;
          }
        }
      }
    }

  }

  .org-info {
    display: block;
    width: 100%;
    font-size: 14px;

    .content {
      height: 120px;
      line-height: 25px;
      width: 300px;
      margin: auto;
    }
  }

  .footer-copyright {
    width: 100%;
    border-top: lavender solid 1px;

    .content {
      font-size: 11px;
      height: 30px;
      line-height: 130px;
      color: grey;
      width: 300px;
      margin: auto;
      display: flex;

      .equal-columns {
        display: flex;
        flex: 1;
        justify-content: space-between;
      }

      .copyright-div {
        display: none;
      }
    }
  }
}

</style>
<script setup lang="ts">
import {ElMessage} from 'element-plus'

function clickZhiliuyun() {
  const router = useRouter();
  router.push("/");
}

function clickOther() {
  ElMessage({
    message: '敬请期待',
    type: 'success',
  })
}

function clickGithubProject() {
  window.open("https://github.com/orgs/isxcode/projects/21");
}

function clickGitee() {
  window.open("https://gitee.com/isxcode/flink-yun");
}

function clickzhiqingyun() {
  window.open("https://zhiqingyun.isxcode.com");
}

function clickZhishuyun() {
  window.open("https://zhishuyun.isxcode.com");
}

function clickZhishiyun() {
  window.open("https://zhishiyun.isxcode.com");
}

function clickZhihuiyun() {
  window.open("https://zhihuiyun.isxcode.com");
}

function clickGithub() {
  window.open("https://github.com/isxcode/flink-yun/discussions");
}

function clickSlack() {
  window.open("https://join.slack.com/t/isxcode/shared_invite/zt-2k9pnkm68-6hJo9e0Mp1yax2mNYv8caA");
}

function clickDiscord() {
  window.open("https://discord.gg/uWHWkGph2t");
}

function clickDockerHub() {
  window.open("https://hub.docker.com/r/isxcode/zhiliuyun");
}

function downloadLicense() {
  window.open("https://isxcode.oss-cn-shanghai.aliyuncs.com/zhiliuyun/license.lic");
}

function downloadPackage() {
  window.open("https://isxcode.oss-cn-shanghai.aliyuncs.com/zhiliuyun/zhiliuyun.tar.gz");
}

function clickFeishu() {
  window.open("https://mwur1opms2a.feishu.cn/wiki/space/7350313682586451971?ccm_open_type=lark_wiki_spaceLink&open_tab_from=wiki_home");
}

function clickBlogs() {
  window.open("https://ispong.isxcode.com/tags/flink/");
}

function clickDocs() {
  const router = useRouter();
  router.push("/docs/zh/0/0");
}

function clickIcp() {
  window.open("https://beian.miit.gov.cn/");
}

function callPhoneNumber() {
  window.location.href = `tel:4008848170`;
}

function aboutUs(){
  const router = useRouter();
  router.push("/docs/zh/8/0");
}

function joinUs(){
  const router = useRouter();
  router.push("/docs/zh/7/0");
}

</script>
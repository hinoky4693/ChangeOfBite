<template>
  <!-- <div class="section-footer">
    <div class="legal-information">
      <div class="copyright-2024-peterdraw">Copyright © 2024 Peterdraw</div>
      <div class="links">
        <div class="privacy-policy">Privacy Policy</div>
        <div class="term-and-conditions">Term and conditions</div>
        <div class="contact">Contact</div>
      </div>
    </div>
    <div class="social-media">
      <img class="facebook-logo" src="../assets/icon/FacebookLogo.svg" />
      <img class="twitter-logo" src="../assets/icon/TwitterLogo.svg" />
      <img class="instagram-logo" src="../assets/icon/InstagramLogo.svg" />
      <img class="youtube-logo" src="../assets/icon/YoutubeLogo.svg" />
      <img class="linkedin-logo" src="../assets/icon/LinkedinLogo.svg" />
    </div>
  </div> -->
</template>

<script setup>
const computed = () => {}
</script>

<style scoped>
.section-footer,
.section-footer * {
  box-sizing: border-box;
}
.section-footer {
  display: flex;
  flex-direction: row;
  gap: 12px;
  align-items: center;
  justify-content: flex-start;
  align-self: stretch;
  flex-shrink: 0;
  height: 20px;
  position: relative;
}
.legal-information {
  display: flex;
  flex-direction: row;
  gap: 24px;
  align-items: flex-start;
  justify-content: flex-start;
  flex: 1;
  position: relative;
}
.copyright-2024-peterdraw {
  color: var(--gray-30, #52545b);
  text-align: left;
  font-family: var(--title-12px-semibold-font-family, 'Poppins-SemiBold', sans-serif);
  font-size: var(--title-12px-semibold-font-size, 12px);
  line-height: var(--title-12px-semibold-line-height, 130%);
  font-weight: var(--title-12px-semibold-font-weight, 600);
  position: relative;
}
.links {
  display: flex;
  flex-direction: row;
  gap: 16px;
  align-items: flex-start;
  justify-content: flex-start;
  flex-shrink: 0;
  position: relative;
}
.privacy-policy {
  color: var(--gray-20, #8a8c90);
  text-align: left;
  font-family: var(--title-12px-regular-font-family, 'Poppins-Regular', sans-serif);
  font-size: var(--title-12px-regular-font-size, 12px);
  line-height: var(--title-12px-regular-line-height, 130%);
  font-weight: var(--title-12px-regular-font-weight, 400);
  position: relative;
}
.term-and-conditions {
  color: var(--gray-20, #8a8c90);
  text-align: left;
  font-family: var(--title-12px-regular-font-family, 'Poppins-Regular', sans-serif);
  font-size: var(--title-12px-regular-font-size, 12px);
  line-height: var(--title-12px-regular-line-height, 130%);
  font-weight: var(--title-12px-regular-font-weight, 400);
  position: relative;
}
.contact {
  color: var(--gray-20, #8a8c90);
  text-align: left;
  font-family: var(--title-12px-regular-font-family, 'Poppins-Regular', sans-serif);
  font-size: var(--title-12px-regular-font-size, 12px);
  line-height: var(--title-12px-regular-line-height, 130%);
  font-weight: var(--title-12px-regular-font-weight, 400);
  position: relative;
}
.social-media {
  display: flex;
  flex-direction: row;
  gap: 12px;
  align-items: flex-start;
  justify-content: flex-start;
  flex-shrink: 0;
  position: relative;
}
.facebook-logo {
  flex-shrink: 0;
  width: 20px;
  height: 20px;
  position: relative;
  overflow: visible;
}
.twitter-logo {
  flex-shrink: 0;
  width: 20px;
  height: 20px;
  position: relative;
  overflow: visible;
}
.instagram-logo {
  flex-shrink: 0;
  width: 20px;
  height: 20px;
  position: relative;
  overflow: visible;
}
.youtube-logo {
  flex-shrink: 0;
  width: 20px;
  height: 20px;
  position: relative;
  overflow: visible;
}
.linkedin-logo {
  flex-shrink: 0;
  width: 20px;
  height: 20px;
  position: relative;
  overflow: visible;
}
</style>

import fs from "node:fs/promises";
import path from "node:path";
import { fileURLToPath } from "node:url";

const artifactModule = process.env.ARTIFACT_TOOL_MODULE || "@oai/artifact-tool";
const { Presentation, PresentationFile } = await import(artifactModule);

const __filename = fileURLToPath(import.meta.url);
const BUILD_DIR = path.dirname(__filename);
const ROOT = process.env.HRMS_DELIVERY_ROOT || path.resolve(BUILD_DIR, "..");
const OUT = path.join(ROOT, "酒店管理系统_答辩PPT初稿.pptx");
const QA_DIR = path.join(ROOT, "_qa", "pptx-preview");

const W = 1280;
const H = 720;
const M = 42;
const INK = "#000000";
const MUTED = "#555555";
const PANEL = "#EDEDED";
const RULE = "#B8BCC4";
const ACCENT = "#FF6B35";
const FONT = "Helvetica Neue";

async function writeBlob(file, blob) {
  await fs.writeFile(file, new Uint8Array(await blob.arrayBuffer()));
}

function addText(slide, text, position, style = {}) {
  const shape = slide.shapes.add({
    geometry: "textbox",
    position,
    fill: "none",
    line: { style: "solid", fill: "none", width: 0 },
  });
  shape.text = text;
  shape.text.style = {
    fontFace: FONT,
    fontSize: style.fontSize ?? 24,
    bold: style.bold ?? false,
    color: style.color ?? INK,
    alignment: style.alignment ?? "left",
  };
  return shape;
}

function addPanel(slide, position, fill = PANEL) {
  return slide.shapes.add({
    geometry: "rect",
    position,
    fill,
    line: { style: "solid", fill: RULE, width: 1 },
  });
}

function addRule(slide, top) {
  slide.shapes.add({
    geometry: "rect",
    position: { left: M, top, width: W - M * 2, height: 1 },
    fill: RULE,
    line: { style: "solid", fill: RULE, width: 0 },
  });
}

function addFooter(slide, num) {
  addText(slide, String(num).padStart(2, "0"), {
    left: W - 86,
    top: H - 62,
    width: 44,
    height: 24,
  }, { fontSize: 16, color: MUTED, alignment: "right" });
}

function titleSlide(presentation, num, title, subtitle, meta) {
  const slide = presentation.slides.add();
  slide.background.fill = "#FFFFFF";
  addText(slide, "酒店管理系统", { left: M, top: 42, width: 360, height: 34 }, { fontSize: 24, bold: true, color: MUTED });
  addText(slide, title, { left: M, top: 170, width: 850, height: 190 }, { fontSize: 64, bold: true });
  addText(slide, subtitle, { left: M, top: 388, width: 760, height: 80 }, { fontSize: 26, color: MUTED });
  addPanel(slide, { left: 866, top: 120, width: 330, height: 420 });
  addText(slide, meta, { left: 900, top: 164, width: 270, height: 320 }, { fontSize: 24 });
  addRule(slide, 620);
  addFooter(slide, num);
}

function agendaSlide(presentation, num) {
  const slide = presentation.slides.add();
  slide.background.fill = "#FFFFFF";
  addText(slide, "答辩从业务闭环展开", { left: M, top: 52, width: 740, height: 58 }, { fontSize: 44, bold: true });
  const items = [
    ["01", "项目目标与角色分工"],
    ["02", "需求覆盖和功能边界"],
    ["03", "架构、数据库和接口设计"],
    ["04", "前端页面和后端联调"],
    ["05", "测试结果、风险与演示"],
  ];
  items.forEach((item, i) => {
    const top = 145 + i * 88;
    addText(slide, item[0], { left: M, top, width: 78, height: 44 }, { fontSize: 34, bold: true, color: ACCENT });
    addText(slide, item[1], { left: 150, top: top + 2, width: 720, height: 44 }, { fontSize: 34 });
    addRule(slide, top + 64);
  });
  addFooter(slide, num);
}

function bulletsSlide(presentation, num, title, lead, bullets) {
  const slide = presentation.slides.add();
  slide.background.fill = "#FFFFFF";
  addText(slide, title, { left: M, top: 48, width: 780, height: 60 }, { fontSize: 42, bold: true });
  addText(slide, lead, { left: M, top: 126, width: 610, height: 92 }, { fontSize: 24, color: MUTED });
  addPanel(slide, { left: 690, top: 118, width: 508, height: 426 });
  bullets.forEach((b, i) => {
    addText(slide, b[0], { left: 725, top: 160 + i * 106, width: 180, height: 30 }, { fontSize: 25, bold: true });
    addText(slide, b[1], { left: 725, top: 196 + i * 106, width: 430, height: 56 }, { fontSize: 19, color: "#222222" });
  });
  addFooter(slide, num);
}

function processSlide(presentation, num) {
  const slide = presentation.slides.add();
  slide.background.fill = "#FFFFFF";
  addText(slide, "预订到清洁形成完整状态流转", { left: M, top: 48, width: 920, height: 60 }, { fontSize: 42, bold: true });
  const steps = [
    ["预订", "校验房型和日期"],
    ["入住", "生成客史、入住记录和账单"],
    ["消费", "追加服务费并重算金额"],
    ["退房", "结算账单并转为脏房"],
    ["清洁", "任务完成后恢复空净"],
  ];
  steps.forEach((s, i) => {
    const left = M + i * 238;
    addPanel(slide, { left, top: 190, width: 198, height: 230 });
    addText(slide, `0${i + 1}`, { left: left + 18, top: 212, width: 70, height: 42 }, { fontSize: 34, bold: true, color: ACCENT });
    addText(slide, s[0], { left: left + 18, top: 282, width: 150, height: 42 }, { fontSize: 32, bold: true });
    addText(slide, s[1], { left: left + 18, top: 342, width: 150, height: 60 }, { fontSize: 18, color: MUTED });
    if (i < steps.length - 1) {
      addText(slide, "→", { left: left + 202, top: 282, width: 30, height: 40 }, { fontSize: 30, color: MUTED });
    }
  });
  addText(slide, "这个流程也是测试和演示的主线。", { left: M, top: 500, width: 720, height: 46 }, { fontSize: 26, color: MUTED });
  addFooter(slide, num);
}

function metricsSlide(presentation, num) {
  const slide = presentation.slides.add();
  slide.background.fill = "#FFFFFF";
  addText(slide, "测试结果证明后端核心链路可运行", { left: M, top: 48, width: 930, height: 60 }, { fontSize: 42, bold: true });
  const metrics = [
    ["6", "JUnit 测试通过"],
    ["22", "核心 API 冒烟断言"],
    ["98", "房间种子数据"],
    ["0", "失败和错误"],
  ];
  metrics.forEach((m, i) => {
    const left = M + i * 300;
    addPanel(slide, { left, top: 178, width: 255, height: 230 });
    addText(slide, m[0], { left: left + 26, top: 210, width: 190, height: 88 }, { fontSize: 70, bold: true });
    addText(slide, m[1], { left: left + 26, top: 318, width: 190, height: 58 }, { fontSize: 20, color: MUTED });
  });
  addText(slide, "未覆盖项保留为后续补测建议：前端 E2E、移动端真机、WebSocket 客户端订阅和并发边界。", { left: M, top: 484, width: 900, height: 72 }, { fontSize: 24, color: MUTED });
  addFooter(slide, num);
}

function tableSlide(presentation, num) {
  const slide = presentation.slides.add();
  slide.background.fill = "#FFFFFF";
  addText(slide, "当前风险集中在配置和工程化", { left: M, top: 48, width: 920, height: 60 }, { fontSize: 42, bold: true });
  const rows = [
    ["风险", "影响", "建议"],
    ["数据库凭据硬编码", "换机器后启动失败", "使用 profile 或环境变量"],
    ["前端地址硬编码", "端口变化需改源码", "代理或条件编译"],
    ["前端缺少 CLI", "CI 无法构建", "补充 package.json"],
    ["WebSocket 未测", "实时推送缺验证", "增加客户端订阅用例"],
  ];
  const top = 150;
  rows.forEach((row, r) => {
    const y = top + r * 78;
    row.forEach((cell, c) => {
      const x = M + c * 386;
      addPanel(slide, { left: x, top: y, width: 360, height: 62 }, r === 0 ? PANEL : "#FFFFFF");
      addText(slide, cell, { left: x + 16, top: y + 14, width: 328, height: 34 }, { fontSize: r === 0 ? 22 : 18, bold: r === 0, color: r === 0 ? INK : "#222222" });
    });
  });
  addFooter(slide, num);
}

function closingSlide(presentation, num) {
  const slide = presentation.slides.add();
  slide.background.fill = "#FFFFFF";
  addText(slide, "演示按登录、房态、入住、退房、报表收束", { left: M, top: 70, width: 1040, height: 118 }, { fontSize: 50, bold: true });
  addText(slide, "结论：后端核心功能已完成并通过冒烟测试，前端页面和文档材料可支持课程答辩；后续重点是配置外置化、前端工程化和更完整的 E2E 测试。", { left: M, top: 250, width: 930, height: 128 }, { fontSize: 26, color: MUTED });
  addPanel(slide, { left: M, top: 455, width: 360, height: 90 });
  addText(slide, "答辩材料", { left: M + 26, top: 482, width: 300, height: 34 }, { fontSize: 25, bold: true });
  addPanel(slide, { left: 460, top: 455, width: 360, height: 90 });
  addText(slide, "演示视频脚本", { left: 486, top: 482, width: 300, height: 34 }, { fontSize: 25, bold: true });
  addPanel(slide, { left: 878, top: 455, width: 320, height: 90 });
  addText(slide, "测试报告", { left: 904, top: 482, width: 260, height: 34 }, { fontSize: 25, bold: true });
  addFooter(slide, num);
}

async function main() {
  await fs.mkdir(QA_DIR, { recursive: true });
  const presentation = Presentation.create({ slideSize: { width: W, height: H } });
  titleSlide(
    presentation,
    1,
    "酒店管理系统完成了核心业务闭环",
    "测试工程师 / 文档助理交付初稿",
    "负责人：XXX\n日期：2026-07-02\n测试记录：019f21ac-5bd7-70f0-ada5-30be4ba097be",
  );
  agendaSlide(presentation, 2);
  bulletsSlide(presentation, 3, "需求覆盖前台、客房和管理三类场景", "系统围绕酒店日常运营设计，重点不是单点页面，而是让房间状态在业务流程中正确流转。", [
    ["前台", "预订、入住、退房、账单和客史。"],
    ["客房", "清洁任务接收、流转和房态恢复。"],
    ["管理", "房态总览、入住率和营收报表。"],
  ]);
  bulletsSlide(presentation, 4, "系统采用前后端分离和后端分层设计", "后端负责业务规则和数据一致性，前端负责页面展示和操作入口。", [
    ["后端", "Spring Boot、MyBatis、统一响应、鉴权拦截。"],
    ["数据库", "MySQL 保存 98 间房和核心业务数据。"],
    ["前端", "uni-app 网页端和 APP 端源码。"],
  ]);
  processSlide(presentation, 5);
  bulletsSlide(presentation, 6, "后端以 MyBatis 和 MySQL 承载真实数据", "项目已从内存演示数据转为数据库持久化，复杂查询由 XML Mapper 承载。", [
    ["数据来源", "`schema.sql` 和 `seed.sql` 可重建测试环境。"],
    ["接口契约", "`code/message/data` 统一响应便于前端处理。"],
    ["业务校验", "房态、预订、入住、退房都在 Service 层校验。"],
  ]);
  bulletsSlide(presentation, 7, "前端提供网页端和 APP 端页面源码", "当前前端更适合通过 HBuilderX 运行和发行，命令行工程化入口是后续补齐重点。", [
    ["网页端", "房态、预订、入住、退房、账单、报表页面。"],
    ["APP 端", "移动端 tabBar 和业务页面源码。"],
    ["联调风险", "API 地址仍需统一配置。"],
  ]);
  metricsSlide(presentation, 8);
  tableSlide(presentation, 9);
  closingSlide(presentation, 10);

  if (process.env.HRMS_ARTIFACT_PREVIEW === "1") {
    for (const [index, slide] of presentation.slides.items.entries()) {
      const stem = `slide-${String(index + 1).padStart(2, "0")}`;
      await writeBlob(path.join(QA_DIR, `${stem}.png`), await presentation.export({ slide, format: "png", scale: 1 }));
      const layout = await slide.export({ format: "layout" });
      await fs.writeFile(path.join(QA_DIR, `${stem}.layout.json`), await layout.text(), "utf-8");
    }
    await writeBlob(path.join(QA_DIR, "deck-montage.webp"), await presentation.export({ format: "webp", montage: true, scale: 1 }));
  }
  const pptx = await PresentationFile.exportPptx(presentation);
  await pptx.save(OUT);
  console.log(OUT);
}

main().catch((error) => {
  console.error(error);
  process.exitCode = 1;
});

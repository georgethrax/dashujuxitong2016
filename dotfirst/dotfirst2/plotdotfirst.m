%df = importdata('dotfirst.1.txt2/part-00000');
df = importdata('dvdotfirst.txt');
bigline=[0,383589 ,451541 ,481001 ,577831,603846,2388018,2507359,2698845,2907399];
figure;
plot(df);
hold on;
plot([bigline;bigline],[-0.2*ones(1,length(bigline));ones(1,length(bigline))],'.-','LineWidth',2);
title('健康 科技 政策 理财 默认大分类 生活 职场 金融 互联网 保险');
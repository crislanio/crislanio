import sys, Queue
from PIL import Image, ImageDraw, ImageFont

class node:
    def __init__(self, x):
        self.chave = x
        self.esq = self.dir = None
        self.ld = self.le = 0
        self.wd = self.we = 0
    def __str__(self):
        return '{0}: ({1},{2}) ({3},{4})'.format(
                         self.chave, 
                         self.le, 
                         self.ld, 
                         self.we, 
                         self.wd)

class tree:
    raio = 21
    delta = raio + raio/3
    dvert = 3*raio 
    cor_fundo = (100,100,100)
    cor_nodo  = (100, 255, 0)
    cor_borda = (0,0,100)
    cor_linha = (0,0,0)
    fonte = ImageFont.truetype('loma.ttf', size=22)
    def __init__(self):
        self.raiz = None
        self.count = 0
        self.dic = {}
    def __load__(self, pai):
        if (pai!=None):
            x, y = self.dic[pai.chave]
            if len(x)>0:
                pai.esq = node( x[0] )
                self.__load__(pai.esq)
            if len(y)>0:
                pai.dir = node( y[0] )
                self.__load__(pai.dir)
    def load(self, fnome):
        f = open(fnome, 'r')
        self.dic.clear()
        lines = f.readlines()
        if len(lines)==0:
            return
        self.raiz = node ( eval(lines[0].split()[0]) )
        print self.raiz.chave
        for line in lines:
            splited = line.split()
            if len(splited)>=3:
                chave, a, b = [eval(s) for s in splited]
                self.dic[chave] = (a, b)
        f.close()
        self.__load__(self.raiz)
    def traverse(self):
        fila = Queue.Queue()
        if self.raiz!=None: 
            fila.put([1, self.raiz])
        while not fila.empty():
            n, x = fila.get()
            print '{0} | {1}'.format(x, n)
            if x.esq!=None: fila.put([n+1, x.esq])
            if x.dir!=None: fila.put([n+1, x.dir])
    def __loadspaces__(self, p):
        if p==None:
            return
        self.__loadspaces__(p.esq)
        self.__loadspaces__(p.dir)
        if p.esq != None:
            p.le = p.esq.wd + self.delta
            p.we = p.le + p.esq.we
        if p.dir != None:
            p.ld = p.dir.we + self.delta
            p.wd = p.ld + p.dir.wd
    def loadspaces(self):
        self.__loadspaces__(self.raiz)
    def __draw__(self, p, x, y, draw):
        if p == None: 
            return
        r = (x - self.raio, y - self.raio,
             x + self.raio, y + self.raio)         
        xe = x - p.le
        xd = x + p.ld
        yy = y + self.dvert
        if p.esq != None:
            draw.line( (x,y,xe,yy), fill=self.cor_linha, width=1)
        if p.dir != None:
            draw.line( (x,y,xd,yy), fill=self.cor_linha, width=1)
        draw.ellipse(r, fill=self.cor_nodo, outline=self.cor_borda)
        label = '{0}'.format(p.chave)
        w, h = draw.textsize(label, font=self.fonte)
        draw.text((x-w/2, y-h/2), label, 
                  font=self.fonte, fill='black')
        self.__draw__(p.esq, xe, yy, draw);
        self.__draw__(p.dir, xd, yy, draw);
    def savetoimage(self, fnome):
        if self.raiz != None:
            self.loadspaces()
            w = 2*max(self.raiz.we, self.raiz.wd) + 2*self.raio
            h = (self.height() - 1)*self.dvert + 2*self.raio
            image = Image.new('RGB', (w, h), self.cor_fundo)
            draw = ImageDraw.ImageDraw(image)
            self.__draw__(self.raiz, w/2, self.raio, draw)
            image.save(fnome)
    def __height__(self, p):
        if p==None:
            return 0
        else:
            h1 = self.__height__(p.esq)
            h2 = self.__height__(p.dir)
            return 1 + max(h1, h2)
    def height(self):
        return self.__height__(self.raiz)

if __name__ == '__main__':
    t = tree()
    if len(sys.argv) < 2:
        sys.exit()
    t.load( sys.argv[1] )
    t.savetoimage(sys.argv[2])
    t.traverse()
    print 'altura = ', t.height()

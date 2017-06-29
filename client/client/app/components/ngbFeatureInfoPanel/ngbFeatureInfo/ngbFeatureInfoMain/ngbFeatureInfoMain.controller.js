export default class ngbFeatureInfoMainController {

    static get UID() {
        return 'ngbFeatureInfoMainController';
    }

    sequence = null;
    sequenceProgress = 0;
    isSequenceLoading = true;
    error = null;

    constructor($scope, genomeDataService, bamDataService, $anchorScroll) {
        Object.assign(this, {$scope, genomeDataService, bamDataService, $anchorScroll});

        if (!this.read) {
            (async() => {this.loadSequence();})();
        }
        else {
            this.isSequenceLoading = false;
        }

        if (this.infoForRead) {
            (async() => {this.loadRead();})();
        }
        else {
            this.isReadLoadingis = false;
        }
    }

    loadSequence() {
        this.isSequenceLoading = true;
        const referenceSize = this.endIndex - this.startIndex;
        const maxReferenceRequestSize = 50000;
        const parts = [];
        for (let i = 0; i < Math.floor(referenceSize / maxReferenceRequestSize) + 1; i++) {
            const start = Math.min(this.startIndex + i * maxReferenceRequestSize, this.endIndex);
            const end = Math.min(start + maxReferenceRequestSize - 1, this.endIndex);
            if (end - start > 0) {
                parts.push({
                    chromosomeId: this.chromosomeId,
                    endIndex: end,
                    id: this.referenceId,
                    scaleFactor: 1,
                    startIndex: start
                });
            }
        }
        const refresh = (result) => {
            this.sequence = result;
            if (!result) {
                this.error = 'Error loading sequence for feature';
            }
            else {
                this.error = null;
            }
            this.isSequenceLoading = false;
            this.$scope.$apply();
        };
        this.getReference(parts, '', 0, refresh);
    }

    loadRead() {
        this.isReadLoadingis = true;
        this.sequenceWithQualities = [];
        this.bamDataService.loadRead(this.infoForRead).then(read => {
            for (let i = 0; i < read.qualities.length; i++) {
                this.sequenceWithQualities.push({nucleotide : read.sequence[i], qualitie: read.qualities.charCodeAt(i) - 33});
            }
            this.tags = read.tags.map(tag => [tag.tag, tag.value]);
            this.isReadLoadingis = false;
        });
    }

    getReference(blocks, referenceBuffer, index, callback) {

        if (index >= blocks.length) {
            callback(referenceBuffer);
            return;
        }
        this.genomeDataService.loadReferenceTrack(blocks[index]).then(data => {
            const refBlocks = data.blocks || [];
            this.sequenceProgress = 100.0 * (index + 1) / blocks.length;
            this.$scope.$apply();
            if (!refBlocks) {
                referenceBuffer = null;
                callback(referenceBuffer);
            }
            else {
                referenceBuffer += refBlocks.reduce((a, b) => a + b.text, '');
                this.getReference(blocks, referenceBuffer, index + 1, callback);
            }
        });
    }

    scrollTo(id) {
        this.$anchorScroll(id);
    }

}
package wanted.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import wanted.logic.commands.BaseEdit.EditLoanDescriptor;
import wanted.model.loan.Loan;
import wanted.model.loan.LoanAmount;
import wanted.model.loan.Name;
import wanted.model.loan.exceptions.ExcessRepaymentException;
import wanted.model.tag.Tag;

/**
 * A utility class to help with building EditLoanDescriptor objects.
 * This is the same as EditPersonDescriptor, with adapted fields
 */
public class EditLoanDescriptorBuilder {

    private EditLoanDescriptor descriptor;

    public EditLoanDescriptorBuilder() {
        descriptor = new EditLoanDescriptor();
    }

    public EditLoanDescriptorBuilder(EditLoanDescriptor descriptor) throws ExcessRepaymentException {
        this.descriptor = new EditLoanDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditLoanDescriptor} with fields containing {@code loan}'s details
     */
    public EditLoanDescriptorBuilder(Loan loan) throws ExcessRepaymentException {
        descriptor = new EditLoanDescriptor();
        descriptor.setName(loan.getName());
        descriptor.setAmount(loan.getLoanAmount().getTransactionHistoryCopy());
        descriptor.setTags(loan.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code EditLoanDescriptor} that we are building.
     */
    public EditLoanDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Amount} of the {@code EditLoanDescriptor} that we are building.
     */
    public EditLoanDescriptorBuilder withLoanAmount(LoanAmount amount) throws ExcessRepaymentException {
        descriptor.setAmount(amount.getTransactionHistoryCopy());
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditLoanDescriptor}
     * that we are building.
     */
    public EditLoanDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditLoanDescriptor build() {
        return descriptor;
    }
}
